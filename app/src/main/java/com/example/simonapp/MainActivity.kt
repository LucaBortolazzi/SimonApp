package com.example.simonapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simonapp.ui.theme.SimonAppTheme


class MainActivity : ComponentActivity() {

    private val viewModel: SimonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SimonAppTheme {
                val navController = rememberNavController()     //utilizzo navigation
                //_______________________________________var finishedGames by rememberSaveable { mutableStateOf(listOf<List<Char>>()) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "GameHistory",
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable("GameHistory"){
                            GameHistoryScreen(
                                viewModel = viewModel,
                                onStartGameClicked = {
                                    navController.navigate("Gameplay") {
                                        popUpTo("GameHistory") { inclusive = false }
                                    }
                                },
                                onGameClicked = { gameId -> navController.navigate("GameDetail/$gameId")}
                                    )
                        }

                        composable("Gameplay"){
                            GameplayScreen(
                                viewModel = viewModel,
                                onNavigateBack = {
                                    navController.navigate("GameHistory"){
                                        popUpTo("GameHistory") { inclusive = true }
                                    }
                                }
                            )
                        }

                        //passo il parametro gameId nella navigazione
                        composable("GameDetail/{gameId}"){  backStackEntry ->
                            val gameId = backStackEntry.arguments?.getString("gameId")?.toIntOrNull()
                            GameDetailScreen(
                                viewModel = viewModel,
                                gameId = gameId
                            )
                        }
                    }
                }
            }
        }
    }
}

//rifai preview

