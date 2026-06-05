package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CharacterEntity::class, CombatRoundEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ShatterpointDatabase : RoomDatabase() {
    abstract fun dao(): ShatterpointDao

    companion object {
        @Volatile
        private var INSTANCE: ShatterpointDatabase? = null

        fun getDatabase(context: Context): ShatterpointDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShatterpointDatabase::class.java,
                    "shatterpoint_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
