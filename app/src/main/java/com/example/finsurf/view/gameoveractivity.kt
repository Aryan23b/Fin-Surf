package com.example.finsurf.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finsurf.R


@Composable
fun GameOverScreen(score: String,navController: NavHostController) {
    val context = LocalContext.current

    // Get high score from SharedPreferences
    val prefs = context.getSharedPreferences("gameData", Context.MODE_PRIVATE)
    val highScore = prefs.getInt("highscore", 0)

    // Main container with background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0061ff), // Blue
                        Color(0xFF60efff), // Lighter blue
                        //Color(0xFF60A5FA)  // Even lighter blue
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column( modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center )
        {
            Image(
                painter = painterResource(id = R.drawable.splashimg2),
                contentDescription = "Background",
                modifier = Modifier.size(150.dp),
                alignment = Alignment.Center
            )
            Text(
                text = "Beat your past ahead with fast ",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = Color(0xFFFF9800),
                textAlign = TextAlign.Center
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .background(Color.Transparent),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0x803D3D3D)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Game Over Title
                    Text(
                        text = "Game Over",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    // Score Display
                    ScoreDisplay(currentScore = score, highScore = highScore)

                    // Play Again Button
                    PlayAgainButton(
                        onClick = {
                            navController.navigate("game")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ScoreDisplay(currentScore: String, highScore: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Current Score
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF3D3D3D)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Your Score",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = currentScore,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // High Score
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF4A4A00) // Subtle yellow tint
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "High Score",
                    fontSize = 16.sp,
                    color = Color(0xFFFFD700), // Gold color
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = highScore.toString(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700) // Gold color
                )
            }
        }
    }
}

@Composable
fun PlayAgainButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50) // Green color
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = "Play Again",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}


