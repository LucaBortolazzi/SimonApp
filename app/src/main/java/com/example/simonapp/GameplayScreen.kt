package com.example.simonapp

import android.content.res.Configuration
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val colorList = listOf(
    'R' to Color.Red,
    'G' to Color.Green,
    'B' to Color.Blue,
    'M' to Color.Magenta,
    'Y' to Color.Yellow,
    'C' to Color.Cyan
)

@Composable
fun GameplayScreen(
    onEndGameClicked: (List<Char>) -> Unit,
    modifier: Modifier = Modifier
){
    //ritorna configurazione corrente
    val orientation = LocalConfiguration.current.orientation

    //sequenza sopravvive a cambi di configurazione
    var sequence by rememberSaveable { mutableStateOf(listOf<Char>()) }

    //layout in base a orientazione corrente
    if(orientation == Configuration.ORIENTATION_LANDSCAPE){

        //LANDSCAPE

        Row(modifier = modifier.fillMaxSize()) {
            ColorsGrid(
                //aggiungo alla sequenza la lettera corrispondente al colore premuto
                onButtonPressed = { letter -> sequence = sequence + letter },
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
                    sequence = sequence,
                    modifier = Modifier.weight(1f)
                )

                ButtonsRow(
                    sequence = sequence,
                    onClearClicked = { sequence = emptyList() },
                    onEndGameClicked = {
                        onEndGameClicked(sequence)      //sequenza mandata a GameHistoryScreen
                        sequence = emptyList()          //azzera sequenza
                    }
                )
            }
        }
    }
    else{

        //PORTRAIT

        Column(modifier = modifier.fillMaxSize()) {
            ColorsGrid(
                onButtonPressed = { letter -> sequence = sequence + letter },
                modifier = Modifier
                    .weight(2f)     //80% schermo
                    .fillMaxWidth()
            )

            SequenceLetter(
                sequence = sequence,
                modifier = Modifier.weight(0.4f)    //20% schermo
            )

            ButtonsRow(
                sequence = sequence,
                onClearClicked = { sequence = emptyList() },
                onEndGameClicked = {
                    onEndGameClicked(sequence)      //sequenza mandata a GameHistoryScreen
                    sequence = emptyList()          //azzera sequenza
                }
            )
        }
    }
}



@Composable
fun ColorsGrid(
    onButtonPressed: (Char) -> Unit,    //comunica quale lettera premuta
    modifier: Modifier = Modifier
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),   //6 elementi
        modifier = modifier.fillMaxSize()
    ){
        items(colorList){ (letter, color) ->    //pair lettera, colore
            ColoredButton(
                letter = letter,
                color = color,
                onClick = { onButtonPressed(letter) }
            )

        }
    }
}

@Composable
fun ColoredButton(
    color: Color,
    letter: Char,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    val orientation = LocalConfiguration.current.orientation

    //gestione dimensione bottoni in base ad orientazione
    val ratio = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 2.5f else 1f

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp)
            .aspectRatio(ratio)
    ){
        Text(
            text = letter.toString(),   //serve string per Text
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SequenceLetter(
    sequence: List<Char>,
    modifier: Modifier = Modifier
) {
    Text(
        //trasforma elementi di Lista in stringa con virgola che separa
        text = sequence.joinToString(", "),
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    )
}

@Composable
fun ButtonsRow(
    onClearClicked: () -> Unit,
    onEndGameClicked: (List<Char>) -> Unit,
    sequence: List<Char>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = onClearClicked) {
            Text(text = stringResource(R.string.btn_clear))
        }
        Button(onClick = { onEndGameClicked (sequence)} ) {
            Text(text = stringResource(R.string.btn_endGame))
        }
    }
}
