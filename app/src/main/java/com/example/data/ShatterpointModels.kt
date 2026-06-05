package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

// Definitions for Shatterpoint Dice faces
enum class AttackDiceFace {
    CRITICAL,   // Crítico
    STRIKE,     // Impacto / Acierto
    EXPERTISE,  // Pericia
    FAILURE     // Fallo / Blanco
}

enum class DefenseDiceFace {
    BLOCK,      // Bloqueo
    EXPERTISE,  // Pericia
    FAILURE     // Fallo / Blanco
}

data class Ability(
    val name: String,
    val type: String, // "Activa", "Reactiva", "Innata", "Identidad"
    val cost: Int, // 0 for innate/reactive usually
    val description: String
)

data class ExpertiseLevel(
    val minExpertise: Int,
    val description: String,
    // What results it creates
    val addedStrikes: Int = 0,
    val addedCrits: Int = 0,
    val addedBlocks: Int = 0,
    val addedDodges: Int = 0
)

data class Stance(
    val name: String,
    val meleeDice: Int,
    val rangedDice: Int,
    val defenseMeleeDice: Int,
    val defenseRangedDice: Int,
    val attackExpertise: List<ExpertiseLevel>,
    val defenseExpertise: List<ExpertiseLevel>
)

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: String, // unique id, like "anakin_skywalker"
    val name: String,
    val title: String,
    val type: String, // "Principal", "Secundario", "Apoyo"
    val faction: String, // "República Galáctica", "Colectivo de Sombra", "Separatista", "Sith", etc.
    val forcePoints: Int, // for principal, 0 otherwise
    val stamina: Int,
    val durability: Int,
    val abilities: List<Ability>,
    val stances: List<Stance>
)

@Entity(tableName = "combat_rounds")
data class CombatRoundEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val attackerId: String,
    val attackerName: String,
    val defenderId: String,
    val defenderName: String,
    val attackerStance: String,
    val defenderStance: String,
    val attackDiceCount: Int,
    val defenseDiceCount: Int,
    // Dice results stored as comma-separated lists of names
    val rawAttackRoll: String, // e.g. "STRIKE,CRITICAL,EXPERTISE,FAILURE"
    val rawDefenseRoll: String, // e.g. "BLOCK,DODGE,EXPERTISE,FAILURE"
    val netStrikes: Int,
    val netCrits: Int,
    val blockedStrikes: Int,
    val attackerNotes: String,
    val defenderNotes: String,
    val roundNotes: String = ""
)

// Converters for complex fields to store in Room using Moshi
class Converters {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @TypeConverter
    fun fromAbilityList(value: List<Ability>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, Ability::class.java)
        val adapter = moshi.adapter<List<Ability>>(type)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toAbilityList(value: String?): List<Ability>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, Ability::class.java)
        val adapter = moshi.adapter<List<Ability>>(type)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun fromStanceList(value: List<Stance>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, Stance::class.java)
        val adapter = moshi.adapter<List<Stance>>(type)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toStanceList(value: String?): List<Stance>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, Stance::class.java)
        val adapter = moshi.adapter<List<Stance>>(type)
        return adapter.fromJson(value)
    }
}
