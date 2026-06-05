package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class ShatterpointViewModel(private val repository: CombatRepository) : ViewModel() {

    // UI state for Characters list
    val characters: StateFlow<List<CharacterEntity>> = repository.allCharacters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI state for Combat History
    val combatHistory: StateFlow<List<CombatRoundEntity>> = repository.allCombatRounds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Roll State
    var selectedAttackerId = MutableStateFlow<String?>(null)
    var selectedDefenderId = MutableStateFlow<String?>(null)
    var selectedAttackerStanceIndex = MutableStateFlow(0)
    var selectedDefenderStanceIndex = MutableStateFlow(0)

    var attackDiceCount = MutableStateFlow(7)
    var defenseDiceCount = MutableStateFlow(5)

    // Current Active Roll Results
    var currentAttackRoll = MutableStateFlow<List<AttackDiceFace>>(emptyList())
    var currentDefenseRoll = MutableStateFlow<List<DefenseDiceFace>>(emptyList())

    // Roll notes/comments
    var attackerNotes = MutableStateFlow("")
    var defenderNotes = MutableStateFlow("")
    var roundNotes = MutableStateFlow("")

    // Simulated statistics results
    val simulationResult = MutableStateFlow<SimulationSummary?>(null)
    val isSimulating = MutableStateFlow(false)
    val isRolling = MutableStateFlow(false)

    // Active selected character for inspection/editing
    var activeInspectCharacter = MutableStateFlow<CharacterEntity?>(null)

    // Reset current rolling workbench
    fun startNewRoll(attacker: CharacterEntity?, defender: CharacterEntity?) {
        selectedAttackerId.value = attacker?.id
        selectedDefenderId.value = defender?.id
        selectedAttackerStanceIndex.value = 0
        selectedDefenderStanceIndex.value = 0
        
        // Auto-fill typical initial dice count
        val aStance = attacker?.stances?.getOrNull(0)
        val dStance = defender?.stances?.getOrNull(0)
        attackDiceCount.value = aStance?.meleeDice ?: 7
        defenseDiceCount.value = dStance?.defenseMeleeDice ?: 5

        currentAttackRoll.value = emptyList()
        currentDefenseRoll.value = emptyList()
        attackerNotes.value = ""
        defenderNotes.value = ""
        roundNotes.value = ""
    }

    // Probabilidad del dado de ataque (8 caras):
    // - 1 Crítico (12.5%): valor 0
    // - 3 Impactos / Strikes (37.5%): valores 1, 2, 3
    // - 2 Pericias / Expertise (25%): valores 4, 5
    // - 2 Fallos / Failure (25%): valores 6, 7 (else)
    private fun randomAttackFace(): AttackDiceFace {
        return when (Random.nextInt(8)) {
            0 -> AttackDiceFace.CRITICAL         // 1 cara: Crítico
            1, 2, 3 -> AttackDiceFace.STRIKE     // 3 caras: Impacto
            4, 5 -> AttackDiceFace.EXPERTISE     // 2 caras: Pericia
            else -> AttackDiceFace.FAILURE       // 2 caras: Fallo
        }
    }

    private fun randomDefenseFace(): DefenseDiceFace {
        return when (Random.nextInt(6)) {
            0, 1 -> DefenseDiceFace.BLOCK
            2, 3 -> DefenseDiceFace.EXPERTISE
            else -> DefenseDiceFace.FAILURE
        }
    }

    // Launch dice rolls randomly with a dynamic rolling animation
    fun rollDice() {
        if (isRolling.value) return
        viewModelScope.launch {
            isRolling.value = true
            // Play a dynamic dice-shaking rolling animation
            for (step in 1..8) {
                currentAttackRoll.value = List(attackDiceCount.value) { randomAttackFace() }
                currentDefenseRoll.value = List(defenseDiceCount.value) { randomDefenseFace() }
                kotlinx.coroutines.delay(65)
            }
            // Final definitive roll
            currentAttackRoll.value = List(attackDiceCount.value) { randomAttackFace() }
            currentDefenseRoll.value = List(defenseDiceCount.value) { randomDefenseFace() }
            isRolling.value = false
        }
    }

    // Toggle/cycle individual attack die value (allows player manual edit like re-rolls or card triggers)
    fun cycleAttackDie(index: Int) {
        val currentList = currentAttackRoll.value.toMutableList()
        if (index in currentList.indices) {
            val nextFace = when (currentList[index]) {
                AttackDiceFace.CRITICAL -> AttackDiceFace.STRIKE
                AttackDiceFace.STRIKE -> AttackDiceFace.EXPERTISE
                AttackDiceFace.EXPERTISE -> AttackDiceFace.FAILURE
                AttackDiceFace.FAILURE -> AttackDiceFace.CRITICAL
            }
            currentList[index] = nextFace
            currentAttackRoll.value = currentList
        }
    }

    // Set specific attack die face
    fun setAttackDie(index: Int, face: AttackDiceFace) {
        val currentList = currentAttackRoll.value.toMutableList()
        if (index in currentList.indices) {
            currentList[index] = face
            currentAttackRoll.value = currentList
        }
    }

    // Toggle/cycle individual defense die value
    fun cycleDefenseDie(index: Int) {
        val currentList = currentDefenseRoll.value.toMutableList()
        if (index in currentList.indices) {
            val nextFace = when (currentList[index]) {
                DefenseDiceFace.BLOCK -> DefenseDiceFace.EXPERTISE
                DefenseDiceFace.EXPERTISE -> DefenseDiceFace.FAILURE
                DefenseDiceFace.FAILURE -> DefenseDiceFace.BLOCK
            }
            currentList[index] = nextFace
            currentDefenseRoll.value = currentList
        }
    }

    // Set specific defense die face
    fun setDefenseDie(index: Int, face: DefenseDiceFace) {
        val currentList = currentDefenseRoll.value.toMutableList()
        if (index in currentList.indices) {
            currentList[index] = face
            currentDefenseRoll.value = currentList
        }
    }

    // Calculate outcomes dynamically based on current roll + character expertise charts
    fun calculateCurrentOutcome(
        attacker: CharacterEntity?,
        defender: CharacterEntity?,
        attackerStanceIdx: Int,
        defenderStanceIdx: Int,
        atkRoll: List<AttackDiceFace>,
        defRoll: List<DefenseDiceFace>
    ): RollResolution {
        val stanceA = attacker?.stances?.getOrNull(attackerStanceIdx)
        val stanceD = defender?.stances?.getOrNull(defenderStanceIdx)

        var totalStrikes = atkRoll.count { it == AttackDiceFace.STRIKE }
        var totalCrits = atkRoll.count { it == AttackDiceFace.CRITICAL }
        val rawAtkExpertise = atkRoll.count { it == AttackDiceFace.EXPERTISE }

        var totalBlocks = defRoll.count { it == DefenseDiceFace.BLOCK }
        val totalDodges = 0
        val rawDefExpertise = defRoll.count { it == DefenseDiceFace.EXPERTISE }

        // Apply Attacker Expertise
        var appliedAtkExpertiseMsg = "Sin tabla pericia"
        var addedStrikesFromExpertise = 0
        var addedCritsFromExpertise = 0
        if (stanceA != null && rawAtkExpertise > 0) {
            val matchedLevel = stanceA.attackExpertise
                .filter { rawAtkExpertise >= it.minExpertise }
                .maxByOrNull { it.minExpertise }
            if (matchedLevel != null) {
                addedStrikesFromExpertise = matchedLevel.addedStrikes
                addedCritsFromExpertise = matchedLevel.addedCrits
                totalStrikes += addedStrikesFromExpertise
                totalCrits += addedCritsFromExpertise
                appliedAtkExpertiseMsg = "${matchedLevel.description} (${rawAtkExpertise}👁️)"
            } else {
                appliedAtkExpertiseMsg = "No alcanza escalón pericia (${rawAtkExpertise}👁️)"
            }
        } else if (rawAtkExpertise > 0) {
            appliedAtkExpertiseMsg = "${rawAtkExpertise}👁️ (Sin modificador)"
        }

        // Apply Defender Expertise
        var appliedDefExpertiseMsg = "Sin tabla pericia"
        var addedBlocksFromExpertise = 0
        var addedDodgesFromExpertise = 0
        if (stanceD != null && rawDefExpertise > 0) {
            val matchedLevel = stanceD.defenseExpertise
                .filter { rawDefExpertise >= it.minExpertise }
                .maxByOrNull { it.minExpertise }
            if (matchedLevel != null) {
                addedBlocksFromExpertise = matchedLevel.addedBlocks
                addedDodgesFromExpertise = matchedLevel.addedDodges
                totalBlocks += addedBlocksFromExpertise
                appliedDefExpertiseMsg = "${matchedLevel.description} (${rawDefExpertise}👁️)"
            } else {
                appliedDefExpertiseMsg = "No alcanza escalón pericia (${rawDefExpertise}👁️)"
            }
        } else if (rawDefExpertise > 0) {
            appliedDefExpertiseMsg = "${rawDefExpertise}👁️ (Sin modificador)"
        }

        // Resolve blocks
        // Standard Block cancels Strike first
        val blockedStrikes = minOf(totalStrikes, totalBlocks)
        val remainingStrikes = maxOf(0, totalStrikes - totalBlocks)
        // Crits cannot be blocked by standard Blocks!
        val remainingCrits = totalCrits

        return RollResolution(
            rawStrikes = atkRoll.count { it == AttackDiceFace.STRIKE },
            rawCrits = atkRoll.count { it == AttackDiceFace.CRITICAL },
            rawExpertiseAtk = rawAtkExpertise,
            appliedAtkExpertiseMsg = appliedAtkExpertiseMsg,
            totalStrikesWithExpertise = totalStrikes,
            totalCritsWithExpertise = totalCrits,
            
            rawBlocks = defRoll.count { it == DefenseDiceFace.BLOCK },
            rawDodges = totalDodges,
            rawExpertiseDef = rawDefExpertise,
            appliedDefExpertiseMsg = appliedDefExpertiseMsg,
            totalBlocksWithExpertise = totalBlocks,
            totalDodgesWithExpertise = totalDodges + addedDodgesFromExpertise,

            blockedStrikes = blockedStrikes,
            netStrikes = remainingStrikes,
            netCrits = remainingCrits,
            totalSuccesses = remainingStrikes + remainingCrits
        )
    }

    // Save active roll to round history
    fun saveActiveRound(
        attacker: CharacterEntity,
        defender: CharacterEntity,
        resolution: RollResolution
    ) {
        val round = CombatRoundEntity(
            attackerId = attacker.id,
            attackerName = attacker.name,
            defenderId = defender.id,
            defenderName = defender.name,
            attackerStance = attacker.stances.getOrNull(selectedAttackerStanceIndex.value)?.name ?: "Por defecto",
            defenderStance = defender.stances.getOrNull(selectedDefenderStanceIndex.value)?.name ?: "Por defecto",
            attackDiceCount = attackDiceCount.value,
            defenseDiceCount = defenseDiceCount.value,
            rawAttackRoll = currentAttackRoll.value.joinToString(",") { it.name },
            rawDefenseRoll = currentDefenseRoll.value.joinToString(",") { it.name },
            netStrikes = resolution.netStrikes,
            netCrits = resolution.netCrits,
            blockedStrikes = resolution.blockedStrikes,
            attackerNotes = attackerNotes.value,
            defenderNotes = defenderNotes.value,
            roundNotes = roundNotes.value
        )
        viewModelScope.launch {
            repository.insertCombatRound(round)
        }
    }

    // Delete a single historical combat round
    fun deleteRound(round: CombatRoundEntity) {
        viewModelScope.launch {
            repository.deleteCombatRound(round)
        }
    }

    // Delete all combat rounds
    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    // Upsert a custom character profile or update existing
    fun saveCharacter(character: CharacterEntity) {
        viewModelScope.launch {
            repository.insertCharacter(character)
        }
    }

    // Run 1000 trials simulation to calculate expected averages & distribution percentages for combat evaluation
    fun runCombatSimulation(
        attacker: CharacterEntity,
        defender: CharacterEntity,
        attackerStanceIdx: Int,
        defenderStanceIdx: Int,
        customAttackDice: Int,
        customDefenseDice: Int
    ) {
        viewModelScope.launch {
            isSimulating.value = true
            
            val trials = 1000
            var cumulativeSuccesses = 0
            var cumulativeStrikes = 0
            var cumulativeCrits = 0
            var cumulativeBlocks = 0
            
            val outcomeDistribution = mutableMapOf<Int, Int>() // successCount -> occurrenceCount

            for (i in 0 until trials) {
                // Simulate attack roll
                val atkRoll = List(customAttackDice) { randomAttackFace() }
                // Simulate defense roll
                val defRoll = List(customDefenseDice) { randomDefenseFace() }

                val resolution = calculateCurrentOutcome(
                    attacker = attacker,
                    defender = defender,
                    attackerStanceIdx = attackerStanceIdx,
                    defenderStanceIdx = defenderStanceIdx,
                    atkRoll = atkRoll,
                    defRoll = defRoll
                )

                cumulativeSuccesses += resolution.totalSuccesses
                cumulativeStrikes += resolution.totalStrikesWithExpertise
                cumulativeCrits += resolution.totalCritsWithExpertise
                cumulativeBlocks += resolution.totalBlocksWithExpertise

                val succ = resolution.totalSuccesses
                outcomeDistribution[succ] = (outcomeDistribution[succ] ?: 0) + 1
            }

            val percentages = outcomeDistribution.mapValues { (_, count) ->
                (count.toFloat() / trials) * 100f
            }.toSortedMap()

            simulationResult.value = SimulationSummary(
                avgSuccesses = cumulativeSuccesses.toFloat() / trials,
                avgStrikes = cumulativeStrikes.toFloat() / trials,
                avgCrits = cumulativeCrits.toFloat() / trials,
                avgBlocks = cumulativeBlocks.toFloat() / trials,
                percentageMap = percentages,
                trialsCount = trials
            )
            isSimulating.value = false
        }
    }
}

// Data holder classes
data class RollResolution(
    val rawStrikes: Int,
    val rawCrits: Int,
    val rawExpertiseAtk: Int,
    val appliedAtkExpertiseMsg: String,
    val totalStrikesWithExpertise: Int,
    val totalCritsWithExpertise: Int,

    val rawBlocks: Int,
    val rawDodges: Int,
    val rawExpertiseDef: Int,
    val appliedDefExpertiseMsg: String,
    val totalBlocksWithExpertise: Int,
    val totalDodgesWithExpertise: Int,

    val blockedStrikes: Int,
    val netStrikes: Int,
    val netCrits: Int,
    val totalSuccesses: Int
)

data class SimulationSummary(
    val avgSuccesses: Float,
    val avgStrikes: Float,
    val avgCrits: Float,
    val avgBlocks: Float,
    val percentageMap: Map<Int, Float>,
    val trialsCount: Int
)

class ShatterpointViewModelFactory(private val repository: CombatRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShatterpointViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShatterpointViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
