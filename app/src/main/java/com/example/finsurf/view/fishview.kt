package com.example.finsurf.view


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import com.example.finsurf.R
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlin.math.floor
import kotlin.random.Random

// Game state data class
data class GameState(
    val fishX: Float = 10f,
    val fishY: Float = 550f,
    val fishSpeed: Float = 22f,
    val yellowX: Float = 0f,
    val yellowY: Float = 0f,
    val yellowSpeed: Float = 16f,
    val greenX: Float = 0f,
    val greenY: Float = 0f,
    val greenSpeed: Float = 26f,
    val redX: Float = 0f,
    val redY: Float = 0f,
    val redSpeed: Float = 20f,
    val score: Int = 0,
    val lifeCounter: Int = 3,
    val isTouch: Boolean = false,
    val isGameOver: Boolean = false,
    val canvasWidth: Float = 0f,
    val canvasHeight: Float = 0f,
    val initialized: Boolean = false
)

@Composable
fun FlyingFishGame(navController: NavHostController) {
    val context = LocalContext.current
    val density = LocalDensity.current

    var gameState by remember { mutableStateOf(GameState()) }

    // Game loop
    LaunchedEffect(gameState.isGameOver) {
        if (!gameState.isGameOver) {
            while (true) {
                delay(50) // ~20 FPS
                gameState = updateGameState(gameState, context,navController)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (!gameState.isGameOver) {
                            gameState = gameState.copy(
                                isTouch = true,
                                fishSpeed = -22f
                            )
                        }
                    }
                )
            }
    ) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.gameoverbk),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Game Canvas
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Initialize game state with canvas dimensions
            if (!gameState.initialized) {
                gameState = gameState.copy(
                    canvasWidth = canvasWidth,
                    canvasHeight = canvasHeight,
                    yellowX = canvasWidth + 21f,
                    greenX = canvasWidth + 21f,
                    redX = canvasWidth + 21f,
                    initialized = true
                )
            }

            // Draw balls
            drawBalls(gameState)
        }

        // Fish (positioned absolutely)
        Image(
            painter = painterResource(
                id = if (gameState.isTouch) R.drawable.fish2 else R.drawable.fish1
            ),
            contentDescription = "Fish",
            modifier = Modifier
                .size(
                    width = with(density) { (gameState.canvasWidth / 7).toDp() },
                    height = with(density) { (gameState.canvasHeight / 12).toDp() }
                )
                .offset(
                    x = with(density) { gameState.fishX.toDp() },
                    y = with(density) { gameState.fishY.toDp() }
                )
        )

        // UI Overlay
        GameUI(gameState = gameState)
    }
}

@Composable
fun GameUI(gameState: GameState) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Score
            Text(
                text = "Score: ${gameState.score}",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            // Lives
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) { index ->
                    Image(
                        painter = painterResource(
                            id = if (index < gameState.lifeCounter) {
                                R.drawable.hearts1
                            } else {
                                R.drawable.heart_grey1
                            }
                        ),
                        contentDescription = "Life",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

fun DrawScope.drawBalls(gameState: GameState) {
    val ballRadius = size.width * 0.03f

    // Yellow ball
    drawCircle(
        color = Color.Yellow,
        radius = ballRadius,
        center = androidx.compose.ui.geometry.Offset(gameState.yellowX, gameState.yellowY)
    )

    // Green ball
    drawCircle(
        color = Color.Green,
        radius = size.width * 0.04f,
        center = androidx.compose.ui.geometry.Offset(gameState.greenX, gameState.greenY)
    )

    // Red ball
    drawCircle(
        color = Color.Red,
        radius = size.width * 0.045f,
        center = androidx.compose.ui.geometry.Offset(gameState.redX, gameState.redY)
    )
}

fun updateGameState(currentState: GameState, context: Context ,navController: NavHostController): GameState {
    if (currentState.isGameOver || !currentState.initialized) return currentState

    val fishWidth = currentState.canvasWidth / 7
    val fishHeight = currentState.canvasHeight / 12
    val minFishY = fishHeight
    val maxFishY = currentState.canvasHeight - fishHeight * 3

    // Update fish position
    var newFishY = currentState.fishY + currentState.fishSpeed
    var newFishSpeed = currentState.fishSpeed + 2

    if (newFishY < minFishY) newFishY = minFishY
    if (newFishY > maxFishY) newFishY = maxFishY

    // Update balls
    var newYellowX = currentState.yellowX - currentState.yellowSpeed
    var newYellowY = currentState.yellowY
    var newGreenX = currentState.greenX - currentState.greenSpeed
    var newGreenY = currentState.greenY
    var newRedX = currentState.redX - currentState.redSpeed
    var newRedY = currentState.redY

    var newScore = currentState.score
    var newLifeCounter = currentState.lifeCounter
    var newIsGameOver = currentState.isGameOver

    // Yellow ball logic
    if (hitBallCheck(currentState.fishX, newFishY, fishWidth, fishHeight, newYellowX, newYellowY)) {
        newScore += 10
        newYellowX = -100f
    }
    if (newYellowX < 0) {
        newYellowX = currentState.canvasWidth + 21f
        newYellowY = minFishY + floor(Random.nextFloat() * (maxFishY - minFishY))
    }

    // Green ball logic
    if (hitBallCheck(currentState.fishX, newFishY, fishWidth, fishHeight, newGreenX, newGreenY)) {
        newScore += 25
        newGreenX = -120f
    }
    if (newGreenX < 0) {
        newGreenX = currentState.canvasWidth + 21f
        newGreenY = minFishY + floor(Random.nextFloat() * (maxFishY - minFishY))
    }

    // Red ball logic
    if (hitBallCheck(currentState.fishX, newFishY, fishWidth, fishHeight, newRedX, newRedY)) {
        newRedX = -100f
        newScore -= 20
        newLifeCounter--

        if (newLifeCounter == 0 && !newIsGameOver) {
            newIsGameOver = true

            // Save high score
            val prefs = context.getSharedPreferences("gameData", Context.MODE_PRIVATE)
            val highScore = prefs.getInt("highscore", 0)

            if (newScore > highScore) {
                val editor = prefs.edit()
                editor.putInt("highscore", newScore)
                editor.apply()
            }
            Toast.makeText(context, "Khel khatam", Toast.LENGTH_SHORT).show()
            navController.navigate("game_over/${newScore}")
        }
    }
    if (newRedX < 0) {
        newRedX = currentState.canvasWidth + 21f
        newRedY = minFishY + floor(Random.nextFloat() * (maxFishY - minFishY))
    }

    return currentState.copy(
        fishY = newFishY,
        fishSpeed = newFishSpeed,
        yellowX = newYellowX,
        yellowY = newYellowY,
        greenX = newGreenX,
        greenY = newGreenY,
        redX = newRedX,
        redY = newRedY,
        score = newScore,
        lifeCounter = newLifeCounter,
        isGameOver = newIsGameOver,
        isTouch = false
    )
}

fun hitBallCheck(fishX: Float, fishY: Float, fishWidth: Float, fishHeight: Float, ballX: Float, ballY: Float): Boolean {
    return fishX < ballX && fishY < ballY && (fishX + fishWidth) > ballX && (fishY + fishHeight) > ballY
}

@Composable
fun GameScreen(navController: NavHostController) {
    FlyingFishGame(navController)
}