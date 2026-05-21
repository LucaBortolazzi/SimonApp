package com.example.simonapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Row

@Composable
fun GameDetailScreen(
    viewModel: SimonViewModel,
    gameId: Int?,
    modifier: Modifier = Modifier
) {
    //osserva lista dal DB
    val games by viewModel.allGames.collectAsState()

    //trova partita con id corrispondente
    val game = games.find { it.id == gameId }

    if (game == null) {
        //partita non trovata
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Partita non trovata")
        }
    } else {
        //si esce con tasto Back di sistema
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 8.dp)
                ) {
                    //lunghezza massima sequenza corretta a sinistra
                    Text(
                        text = game.maxCorrectLength.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(50.dp)
                    )

                    //sequenza completa con parte errata in rosso a destra
                    //mostra tutta la sequenza
                    Text(
                        text = buildSequenceText(game.sequence, game.errorIndex),
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}