package com.example.simonapp

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//colori con indice intero invece di Char per coerenza con ViewModel
val colorList = listOf(
    0 to Color.Red,
    1 to Color.Green,
    2 to Color.Blue,
    3 to Color.Magenta,
    4 to Color.Yellow,
    5 to Color.Cyan
)

//lettere corrispondenti agli indici
val colorLabels = listOf("R", "G", "B", "M", "Y", "C")

@Composable
fun GameplayScreen(
    viewModel: SimonViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
){

    //resetta lo stato ogni volta che si entra nella schermata
     LaunchedEffect(Unit) {     //eseguito una sola volta
         viewModel.resetGame()
     }

    //osserva gli stati dal ViewModel
    val gameState by viewModel.gameState.collectAsState()
    val playerSequence by viewModel.playerSequence.collectAsState()
    val activeButton by viewModel.activeButton.collectAsState()

    //ritorna configurazione corrente
    val orientation = LocalConfiguration.current.orientation

    //errore quando il giocatore sbaglia
    if (gameState == GameState.ERROR) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Errore!") },
            text = { Text("Hai premuto il colore sbagliato!") },
            confirmButton = {
                TextButton(onClick = { onNavigateBack() }) {
                    Text("Torna alla lista")
                }
            }
        )
    }

    //layout in base a orientazione corrente
    if(orientation == Configuration.ORIENTATION_LANDSCAPE){



        //______________________________________LANDSCAPE_______________________________________

        Row(modifier = modifier.fillMaxSize()) {
            ColorsGrid(
                activeButton = activeButton,
                //aggiungo alla sequenza la lettera corrispondente al colore premuto
                onButtonPressed = { index -> viewModel.onPlayerInput(index) },
                //bottoni premibili solo durante turno del giocatore
                enabled = gameState == GameState.PLAYER_TURN,
                modifier = Modifier
                    .weight(1.5f)   //60% dello schermo
                    .fillMaxHeight()
            )
            Column(
                modifier = Modifier
                    .weight(1f)     //40% rimanente dello schermo
                    .fillMaxHeight()
                    .padding(8.dp)
            ){
                //margine superiore
                Spacer(modifier = Modifier.height(40.dp))

                SequenceLetter(
                    gameState = gameState,
                    playerSequence = playerSequence,
                    modifier = Modifier.weight(1f)
                )

                //linea separatrice
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                ButtonsRow(
                    gameState = gameState,
                    onStartClicked = { viewModel.startGame() },
                    onPauseResumeClicked = { viewModel.pauseResume() },
                    onEndGameClicked = {
                        viewModel.endGame()
                        onNavigateBack()
                    }
                )
            }
        }
    }
    else{


        //________________________________PORTRAIT___________________________________

        Column(modifier = modifier.fillMaxSize()) {
            ColorsGrid(
                activeButton = activeButton,
                onButtonPressed = { index -> viewModel.onPlayerInput(index) },
                enabled = gameState == GameState.PLAYER_TURN,
                modifier = Modifier
                    .weight(2f)     //80% schermo
                    .fillMaxWidth()
            )

            SequenceLetter(
                gameState = gameState,
                playerSequence = playerSequence,
                modifier = Modifier.weight(0.4f)    //20% schermo
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray
            )

            ButtonsRow(
                gameState = gameState,
                onStartClicked = { viewModel.startGame() },
                onPauseResumeClicked = { viewModel.pauseResume() },
                onEndGameClicked = {
                    viewModel.endGame()
                    onNavigateBack()
                }
            )
        }
    }
}

//matrice di bottoni colorati
@Composable
fun ColorsGrid(
    activeButton: Int,
    onButtonPressed: (Int) -> Unit,    //comunica quale indice premuto
    enabled: Boolean,
    modifier: Modifier = Modifier
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),   //6 elementi
        modifier = modifier.fillMaxSize()
    ){
        items(colorList){ (index, color) ->    //pair indice, colore
            ColoredButton(
                index = index,
                color = color,
                isActive = activeButton == index,
                onClick = { onButtonPressed(index) },
                enabled = enabled
            )
        }
    }
}

//bottoni colorati
@Composable
fun ColoredButton(
    color: Color,
    index: Int,
    isActive: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    val orientation = LocalConfiguration.current.orientation

    //gestione dimensione bottoni in base ad orientazione
    val ratio = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 2.5f else 1f

    //feedback visivo: colore pieno se attivo, trasparente altrimenti
    val displayColor = if (isActive) color else color.copy(alpha = 0.4f)

    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = displayColor,
            disabledContainerColor = displayColor   //mantieni colore anche se disabilitato
        ),
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(25.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp)
            .aspectRatio(ratio)
    ){
        Text(
            text = colorLabels[index],   //serve string per Text
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

//sequenza di lettere corrispondenti a bottone premuto
@Composable
fun SequenceLetter(
    gameState: GameState,
    playerSequence: List<Int>,
    modifier: Modifier = Modifier
) {
    //area vuota durante turno computer
    //sequenza premuta visible durante turno guocatore
    val text = when (gameState) {
        GameState.COMPUTER_TURN, GameState.PAUSED -> ""
        else -> playerSequence.joinToString(", ") { colorLabels[it] }
    }

    Text(
        //trasforma elementi di Lista in stringa con virgola che separa
        text = text,
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    )
}

//3 pulsanti per gestione partita
@Composable
fun ButtonsRow(
    gameState: GameState,
    onStartClicked: () -> Unit,
    onPauseResumeClicked: () -> Unit,
    onEndGameClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        //attivo solo quando partita non è ancora iniziata
        Button(
            onClick = onStartClicked,
            enabled = gameState == GameState.IDLE
        ) {
            Text(text = "Avvia partita")
        }

        //attivo solo durante turno computer, testo cambia in base allo stato
        Button(
            onClick = onPauseResumeClicked,
            enabled = gameState == GameState.COMPUTER_TURN || gameState == GameState.PAUSED
        ) {
            Text(text = if (gameState == GameState.PAUSED) "Riprendi" else "Pausa")
        }

        //attivo durante tutta la partita
        Button(
            onClick = onEndGameClicked,
            enabled = gameState == GameState.COMPUTER_TURN ||
                    gameState == GameState.PAUSED ||
                    gameState == GameState.PLAYER_TURN
        ) {
            Text(text = "Fine partita")
        }
    }
}
