package com.example.simonapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simonapp.ui.theme.SimonAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SimonAppTheme {
                val navController = rememberNavController()     //utilizzo navigation
                var finishedGames by rememberSaveable { mutableStateOf(listOf<List<Char>>()) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "Gameplay",
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable("Gameplay"){
                            GameplayScreen(
                                onEndGameClicked = { completedSequence ->
                                    finishedGames = finishedGames + listOf(completedSequence)
                                    navController.navigate("GameHistory")}
                                    //viene passata la sequenza dopo che viene premuto "fine partia"
                                    //sequenza accodata alle precedenti
                            )
                        }

                        composable("GameHistory"){
                            GameHistoryScreen(finishedGames = finishedGames)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameplayScreenPreview() {
    SimonAppTheme {
        GameplayScreen(
            onEndGameClicked = {}
        )
    }
}
