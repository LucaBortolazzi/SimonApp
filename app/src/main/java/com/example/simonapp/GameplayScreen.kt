package com.example.simonapp

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
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
    "R" to Color.Red,
    "G" to Color.Green,
    "B" to Color.Blue,
    "M" to Color.Magenta,
    "Y" to Color.Yellow,
    "C" to Color.Cyan
)

@Composable
fun ColorsGrid(
    onButtonPressed: () -> Unit,
    modifier: Modifier = Modifier
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
    ){
        items(/*lista di colori ?*/){  //sistemare!!!

        }
    }
}

//bottoni colorati

//sequenza colori

@Composable
fun GameplayScreen(onEndGameClicked: () -> Unit){
    val orientation = LocalConfiguration.current.orientation

    var sequence by rememberSaveable { mutableStateOf(listOf<String>()) }

    if(orientation == Configuration.ORIENTATION_LANDSCAPE){

        //LANDSCAPE

    }
    else{

        //PORTRAIT

    }
}
