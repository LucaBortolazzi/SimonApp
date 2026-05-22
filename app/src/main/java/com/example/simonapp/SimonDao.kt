package com.example.simonapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SimonDao {
    @Insert
    suspend fun insertGame(game: SimonEntity)

    @Query("SELECT * FROM games ORDER BY id DESC")      //sequenze  visualizzate tipo stack
    fun getAllGames(): Flow<List<SimonEntity>>
}