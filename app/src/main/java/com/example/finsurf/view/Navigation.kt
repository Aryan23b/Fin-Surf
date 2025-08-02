package com.example.finsurf.view

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Appnavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splashscreen"
    ){
        composable("splashscreen"){
            SplashScreen(navController)
        }
        composable("game"){
            GameScreen(navController)
        }
        composable("game_over/{score}") { backStackEntry ->
            val score = backStackEntry.arguments?.getString("score") ?: "0"
            GameOverScreen(score = score, navController = navController)
        }
    }
}