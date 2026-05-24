package com.example.simonapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameHistoryScreen(
    viewModel: SimonViewModel,
    onStartGameClicked: () -> Unit,
    onGameClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    //osserva lista da DB e aggiorna automaticamente
    val games by viewModel.allGames.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {

        //pulsante nuova partita in alto a destra
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Button(onClick = onStartGameClicked) {
                Text(text = stringResource(R.string.btn_new_game))
            }
        }

        //lista partite
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Top
            ) {
                itemsIndexed(games) { _, game ->
                    GameHistoryItem(
                        game = game,
                        onClick = { onGameClicked(game.id) }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            //scritta quando lista vuota
            if (games.isEmpty()) {
                Text(
                    text = stringResource(R.string.empty_list),
                    color = Color.Gray,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun GameHistoryItem(
    game: SimonEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 4.dp)
    ) {
        //a sinistra lunghezza massima sequenza corretta
        Text(
            text = game.maxCorrectLength.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(40.dp)
        )

        //a destra sequenza con parte errata in rosso
        Text(
            text = buildSequenceText(game.sequence, game.errorIndex),
            fontSize = 18.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.weight(1f),
            maxLines = 1,                       //massimo 1 riga per sequenza
            overflow = TextOverflow.Ellipsis    //per aggiungere ... se troppo lunga
        )
    }
}

//crea sequenza con parte errata in rosso
fun buildSequenceText(sequence: String, errorIndex: Int) = buildAnnotatedString {
    sequence.forEachIndexed { index, char ->
        if (index >= errorIndex) {
            //dall'errore in poi in rosso
            withStyle(style = SpanStyle(color = Color.Red)) {
                append(char)
            }
        } else {
            //colore normal per parte giusta
            append(char)
        }
    }
}