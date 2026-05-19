package com.example.simonapp

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class SimonRepository(private val simonDao: SimonDao) {

    //flusso delle partite aggiornato automaticamente
    val allGames: Flow<List<SimonEntity>> = simonDao.getAllGames()

    @WorkerThread
    suspend fun insertGame(game: SimonEntity) {
        simonDao.insertGame(game)
    }
}