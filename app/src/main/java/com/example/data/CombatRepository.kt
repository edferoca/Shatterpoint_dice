package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CombatRepository(private val dao: ShatterpointDao) {

    init {
        // Automatically check and insert seed characters if empty on a background scope
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val existing = dao.getAllCharacters().firstOrNull()
                if (existing.isNullOrEmpty()) {
                    dao.insertCharacters(ShatterpointSeedData.coreBoxCharacters)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val allCharacters: Flow<List<CharacterEntity>> = dao.getAllCharacters()

    val allCombatRounds: Flow<List<CombatRoundEntity>> = dao.getAllCombatRounds()

    suspend fun getCharacterById(id: String): CharacterEntity? {
        return dao.getCharacterById(id)
    }

    suspend fun insertCharacter(character: CharacterEntity) {
        dao.insertCharacter(character)
    }

    suspend fun insertCombatRound(round: CombatRoundEntity) {
        dao.insertCombatRound(round)
    }

    suspend fun deleteCombatRound(round: CombatRoundEntity) {
        dao.deleteCombatRound(round)
    }

    suspend fun clearHistory() {
        dao.clearAllCombatRounds()
    }
}
