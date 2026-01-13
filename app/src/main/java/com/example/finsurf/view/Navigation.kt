package com.example.finsurf.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

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
        composable("difficulty_selection"){
            DifficultyScreen(navController)
        }
        composable(
            "game/{difficulty}",
            arguments = listOf(navArgument("difficulty") { type = NavType.StringType })
        ) { backStackEntry ->
            val difficulty = backStackEntry.arguments?.getString("difficulty") ?: "medium"
            GameScreen(navController, difficulty)
        }
        composable(
            "game_over/{score}/{difficulty}",
            arguments = listOf(
                navArgument("score") { type = NavType.StringType },
                navArgument("difficulty") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getString("score") ?: "0"
            val difficulty = backStackEntry.arguments?.getString("difficulty") ?: "medium"
            GameOverScreen(score = score, difficulty = difficulty, navController = navController)
        }
    }
}