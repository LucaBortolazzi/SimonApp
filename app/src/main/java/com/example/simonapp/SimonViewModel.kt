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

//indici dei 6 colori 0=R, 1=G, 2=B, 3=M, 4=Y, 5=C

enum class GameState {
    IDLE,           //partita non ancora iniziata
    COMPUTER_TURN,  //il computer mostra sequenza
    PAUSED,         //pausa durante turno computer
    PLAYER_TURN,    //turno player
    ERROR,          //errore giocatore
    FINISHED        //partita terminata normalmente (fine partita premuto)
}


//utilizzo context Application di AndroidViewModel
class SimonViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SimonRepository(
        SimonDatabase.getDatabase(application).simonDao()
    )

    //lista partite osservabile che si aggiorna ad ogni cambio del DB
    //.stateIn() per converitre in stateFlow
    val allGames: StateFlow<List<SimonEntity>> = repository.allGames
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())



    //_variabile: modificabile solo da viewModel
    // variabile: solo leggibile da UI
    //stati del gioco:
    private val _gameState = MutableStateFlow(GameState.IDLE)
    val gameState: StateFlow<GameState> = _gameState

    //sequenza generata dal computer (lista indici che rappresentano un colore)
    private val _computerSequence = MutableStateFlow<List<Int>>(emptyList())
    val computerSequence: StateFlow<List<Int>> = _computerSequence

    //sequenza premuta dal giocatore (stessa struttura di computerSequence)
    private val _playerSequence = MutableStateFlow<List<Int>>(emptyList())
    val playerSequence: StateFlow<List<Int>> = _playerSequence

    //indice bottone che si deve illuminare (default: -1)
    private val _activeButton = MutableStateFlow<Int>(-1)
    val activeButton: StateFlow<Int> = _activeButton

    //indice primo errore nella sequenza (default: -1)
    private val _errorIndex = MutableStateFlow<Int>(-1)
    val errorIndex: StateFlow<Int> = _errorIndex

    //lunghezza massima sequenza corrretta
    private var maxCorrectLength = 0

    //riferimento coroutine di sequenza del computer
    private var playbackJob: Job? = null

    private var playerInputJob: Job? = null
    private var gameActive = false

    //indice per pausa
    private var pausedAtIndex = 0




    //metodi per azioni:

    fun startGame() {
        //reset completo stato precedente
        playbackJob?.cancel()
        playerInputJob?.cancel()
        gameActive = true

        _gameState.value = GameState.COMPUTER_TURN
        _computerSequence.value = emptyList()   //azzero sequenza del computer
        _playerSequence.value = emptyList()     //azzero sequenza del player
        _errorIndex.value = -1                  //no errori
        _activeButton.value = -1
        maxCorrectLength = 0                    //reset punteggio
        pausedAtIndex = 0
        addNextColorAndPlay()
    }

    //aggiunge colore casuale a sequenza e avvia riproduzione
    private fun addNextColorAndPlay() {
        val next = (0..5).random()      //colore casuale poi metto in coda
        _computerSequence.value = _computerSequence.value + next
        _playerSequence.value = emptyList()
        pausedAtIndex = 0
        playSequenceFrom(0)
    }

    //riproduce sequenza computer a partire da fromIndex
    private fun playSequenceFrom(fromIndex: Int) {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {

            if (fromIndex == 0) delay(800)
            val seq = _computerSequence.value
            for (i in fromIndex until seq.size) {
                if (!gameActive) {
                    return@launch
                }
                pausedAtIndex = i                  //salvataggio posizione corrente
                _activeButton.value = seq[i]
                delay(500)              //bottone illuminato per 600ms
                _activeButton.value = -1
                delay(300)              //pausa tra colore e l'altro
            }
            //turno giocatore
            if (gameActive) {
                _gameState.value = GameState.PLAYER_TURN
            }
            pausedAtIndex = seq.size   //reset
        }
    }

    fun pauseResume() {
        when (_gameState.value) {
            GameState.COMPUTER_TURN -> {
                //pausa: cancella coroutine e salva posizione
                playbackJob?.cancel()
                _activeButton.value = -1
                _gameState.value = GameState.PAUSED
            }
            GameState.PAUSED -> {
                //riprendi da posizione salvata con turno computer
                _gameState.value = GameState.COMPUTER_TURN
                playSequenceFrom(pausedAtIndex)
            }
            else -> {}
        }
    }

    //quando giocatore preme un bottone
    fun onPlayerInput(colorIndex: Int) {
        if (_gameState.value != GameState.PLAYER_TURN) return   //ritorna se non è turno giusto

        val newPlayerSeq = _playerSequence.value + colorIndex
        _playerSequence.value = newPlayerSeq

        _activeButton.value = colorIndex
        viewModelScope.launch {
            delay(100)
            _activeButton.value = -1
        }

        val pos = newPlayerSeq.size - 1  //posizione appena premuta
        val expected = _computerSequence.value[pos]

        if (colorIndex != expected) {    //errore: il giocatore ha sbagliato
            //l'indice errore è nella sequenza completa del computer
            _errorIndex.value = pos
            _gameState.value = GameState.ERROR
            saveGame()
        } else if (newPlayerSeq.size == _computerSequence.value.size) {
            //sequenza completata correttamete
            maxCorrectLength = _computerSequence.value.size
            playerInputJob?.cancel()
            playerInputJob = viewModelScope.launch {
                delay(600)
                if (gameActive) {  // usa gameActive invece di controllare gameState
                    _gameState.value = GameState.COMPUTER_TURN
                    addNextColorAndPlay()
                }
            }
        }
    }

    //quando giocatore preme "Fine Partita"
    fun endGame() {
        gameActive = false
        playbackJob?.cancel()
        playerInputJob?.cancel()
        _activeButton.value = -1
        val seq = _computerSequence.value

        //se ancora prima sequenza (lunghezza 1) e il computer non ha ancora finito di mostrarla, non salvo nulla
        if (seq.size <= 1 && _gameState.value == GameState.COMPUTER_TURN) {
            _gameState.value = GameState.FINISHED
            return
        }

        //errore dopo l'ultimo elemento corretto
        val forcedErrorIndex = _playerSequence.value.size

        //sequenza completa come stringa
        val sequenceString = seq.map { indexToChar(it) }.joinToString("")

        val errorIdx = if (_errorIndex.value >= 0) _errorIndex.value else forcedErrorIndex
        val maxCorrect = maxCorrectLength

        _gameState.value = GameState.FINISHED

        viewModelScope.launch {
            repository.insertGame(
                SimonEntity(
                    id = 0,  // autoGenerate
                    sequence = sequenceString,
                    errorIndex = errorIdx,
                    maxCorrectLength = maxCorrect
                )
            )
        }
    }

    fun resetGame() {
        gameActive = false
        playbackJob?.cancel()
        playerInputJob?.cancel()
        _gameState.value = GameState.IDLE
        _computerSequence.value = emptyList()
        _playerSequence.value = emptyList()
        _activeButton.value = -1
        _errorIndex.value = -1
        maxCorrectLength = 0
        pausedAtIndex = 0
    }

    //salvataggio automatico dopo errore
    private fun saveGame() {
        val seq = _computerSequence.value
        val sequenceString = seq.map { indexToChar(it) }.joinToString("")
        viewModelScope.launch {
            repository.insertGame(
                SimonEntity(
                    id = 0,
                    sequence = sequenceString,
                    errorIndex = _errorIndex.value,
                    maxCorrectLength = maxCorrectLength
                )
            )
        }
    }

    //conversione indice 0-5 nella lettera corrispondente
    private fun indexToChar(index: Int): Char {
        return listOf('R', 'G', 'B', 'M', 'Y', 'C')[index]
    }
}