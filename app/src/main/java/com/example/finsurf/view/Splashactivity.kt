package com.example.finsurf.view

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finsurf.MainActivity
import com.example.finsurf.R

//not in use have different ui for splash screen
@Composable
fun SplashScreen1(navController: NavHostController) {
    val context = LocalContext.current

    // Function to navigate to MainActivity
    fun navigateToMain() {
        val mainIntent = Intent(context, MainActivity::class.java)
        context.startActivity(mainIntent)
        (context as? ComponentActivity)?.finish()
    }

    // Main splash screen layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E3A8A), // Blue
                        Color(0xFF3B82F6), // Lighter blue
                        Color(0xFF60A5FA)  // Even lighter blue
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top spacer
            Spacer(modifier = Modifier.height(60.dp))

            // Game content area
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Game Logo/Icon (if you have one)
                // You can replace this with your actual game logo
                Card(
                    modifier = Modifier.size(120.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.9f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        // Replace with your game icon
                        Text(
                            text = "🐠",
                            fontSize = 60.sp
                        )
                    }
                }

                // Game title
                Text(
                    text = "Flying Fish",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                // Subtitle
                Text(
                    text = "Catch the fish, avoid the danger!",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }

            // Start button
            Button(
                onClick = { navigateToMain() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF1E3A8A)
                ),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(
                    text = "Start Game",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}




@Composable
fun SplashScreen(navController: NavHostController) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
         Image(
             painter = painterResource(id = R.drawable.splashback),
             contentDescription = "Background",
             modifier = Modifier.fillMaxSize(),
             contentScale = ContentScale.Crop
         )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Image(
                    painter = painterResource(id = R.drawable.splashimg2),
                    contentDescription = "Background",
                    modifier = Modifier.size(250.dp),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Flying Fish",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "Get ready for adventure!",
                        fontSize = 18.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                Button(
                    onClick = {
                        navController.navigate("game")

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF097C87)
                    ),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text(
                        text = "Start Game",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
