package com.example.simonapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SimonEntity::class], version = 1, exportSchema = false)
abstract class SimonDatabase : RoomDatabase() {
    abstract fun simonDao(): SimonDao

    companion object {
        @Volatile
        private var INSTANCE: SimonDatabase? = null

        fun getDatabase(context: Context): SimonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SimonDatabase::class.java,
                    "simon_database"
                ).build()
                INSTANCE = instance
                instance    //return instance
            }
        }
    }
}