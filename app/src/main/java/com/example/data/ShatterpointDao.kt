package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShatterpointDao {
    // Character Methods
    @Query("SELECT * FROM characters ORDER BY name ASC")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :charId LIMIT 1")
    suspend fun getCharacterById(charId: String): CharacterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity)

    // Combat Round Methods
    @Query("SELECT * FROM combat_rounds ORDER BY timestamp DESC")
    fun getAllCombatRounds(): Flow<List<CombatRoundEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCombatRound(round: CombatRoundEntity)

    @Delete
    suspend fun deleteCombatRound(round: CombatRoundEntity)

    @Query("DELETE FROM combat_rounds")
    suspend fun clearAllCombatRounds()
}
