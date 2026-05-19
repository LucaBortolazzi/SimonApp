package com.example.simonapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// indici dei 6 colori (corrispondono all'ordine di colorList in GameplayScreen)
// 0=R, 1=G, 2=B, 3=M, 4=Y, 5=C

enum class GameState {
    IDLE,           //partita non ancora iniziata
    COMPUTER_TURN,  //il computer sta mostrando la sequenza
    PLAYER_TURN,    //il giocatore deve replicare
    ERROR,          //il giocatore ha sbagliato
    FINISHED        //partita terminata normalmente (fine partita premuto)
}

class SimonViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SimonRepository(
        SimonDatabase.getDatabase(application).simonDao()
    )

    val allGames: StateFlow<List<SimonEntity>> = repository.allGames
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    //active button
    //index pause





    private val _gameState = MutableStateFlow(GameState.IDLE)
    val gameState: StateFlow<GameState> = _gameState

    private val _computerSequence = MutableStateFlow<List<Int>>(emptyList())
    val computerSequence: StateFlow<List<Int>> = _computerSequence


    //FUNZIONI

    fun startGame() {
        _gameState.value = GameState.COMPUTER_TURN
        _computerSequence.value = emptyList()
        //_ playersequence
    }


    //play sequence

    //pause

    //end game

    //save game


}