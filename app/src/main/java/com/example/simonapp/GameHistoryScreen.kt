package com.example.simonapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameHistoryScreen(
    finishedGames: List<List<Char>>,
    modifier: Modifier = Modifier
){
    //stesso comportamento per portrait e landscape
    SequenceHistory(
        finishedGames = finishedGames,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
//contenitore delle sequenze di partite precedenti
fun SequenceHistory(
    finishedGames: List<List<Char>>,
    modifier: Modifier = Modifier
){
    //utilizzo di lazyColumn per gestire liste di lunghezza variabile,
    //carica solo elementi visibili
    LazyColumn(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center
    ){
        //itero su partite concluse, ignoro indice
        itemsIndexed(finishedGames){ _, game ->
            SequenceItems(
                game = game
            )

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}


@Composable
//stringa/sequenza precedente
fun SequenceItems(
    game: List<Char>,
    modifier: Modifier = Modifier
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
    ){
        Text(
            text = game.size.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(40.dp)
        )

        Text(
            text = game.toString(),
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            maxLines = 1,                       //massimo 1 riga per sequenza
            overflow = TextOverflow.Ellipsis    //per aggiungere ... se troppo lunga
        )
    }
}