package com.example.finsurf.view


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finsurf.R
import kotlinx.coroutines.delay
import kotlin.random.Random

// Data class for difficulty
data class Difficulty(val name: String, val gravity: Float, val lift: Float, val obstacleSpeedMultiplier: Float)

// Game state
data class GameState(
    val fishX: Float = 10f,
    val fishY: Float = 550f,
    val fishVelocity: Float = 0f,
    val difficulty: Difficulty,
    val yellowX: Float = 0f,
    val yellowY: Float = 0f,
    val yellowSpeed: Float,
    val greenX: Float = 0f,
    val greenY: Float = 0f,
    val greenSpeed: Float,
    val redX: Float = 0f,
    val redY: Float = 0f,
    val redSpeed: Float,
    val score: Int = 0,
    val lifeCounter: Int = 3,
    val isGameOver: Boolean = false,
    val canvasWidth: Float = 0f,
    val canvasHeight: Float = 0f,
    val initialized: Boolean = false
)

fun getDifficultySettings(difficulty: String): Difficulty {
    return when (difficulty.lowercase()) {
        "easy" -> Difficulty(name = "easy", gravity = 1.2f, lift = -25f, obstacleSpeedMultiplier = 0.8f)
        "medium" -> Difficulty(name = "medium", gravity = 1.5f, lift = -28f, obstacleSpeedMultiplier = 1.0f)
        "hard" -> Difficulty(name = "hard", gravity = 1.8f, lift = -32f, obstacleSpeedMultiplier = 1.2f)
        else -> Difficulty(name = "medium", gravity = 1.5f, lift = -28f, obstacleSpeedMultiplier = 1.0f) // Default to medium
    }
}

@Composable
fun FlyingFishGame(navController: NavHostController, difficulty: String) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val difficultySettings = getDifficultySettings(difficulty)

    var gameState by remember { mutableStateOf(GameState(
        difficulty = difficultySettings,
        yellowSpeed = 16f * difficultySettings.obstacleSpeedMultiplier,
        greenSpeed = 26f * difficultySettings.obstacleSpeedMultiplier,
        redSpeed = 20f * difficultySettings.obstacleSpeedMultiplier
    )) }

    // Game loop
    LaunchedEffect(gameState.isGameOver) {
        if (!gameState.isGameOver) {
            var lastFrameTime = System.nanoTime()
            while (true) {
                val currentFrameTime = System.nanoTime()
                val deltaTime = (currentFrameTime - lastFrameTime) / 1_000_000_000.0f
                lastFrameTime = currentFrameTime

                delay(16)
                gameState = updateGameState(gameState, context, navController, deltaTime)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        if (!gameState.isGameOver) {
                            gameState = gameState.copy(fishVelocity = gameState.difficulty.lift)
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
                    fishY = canvasHeight / 2,
                    yellowX = canvasWidth + 21f,
                    greenX = canvasWidth + 21f,
                    redX = canvasWidth + 21f,
                    initialized = true
                )
            }

            // Draw balls
            drawBalls(gameState)
        }

        if (gameState.initialized) {
            Image(
                painter = painterResource(
                    id = if (gameState.fishVelocity < 0) R.drawable.fish2 else R.drawable.fish1
                ),
                contentDescription = "Fish",
                modifier = Modifier
                    .size(
                        width = with(density) { (gameState.canvasWidth / 8).toDp() },
                        height = with(density) { (gameState.canvasHeight / 14).toDp() }
                    )
                    .offset(
                        x = with(density) { gameState.fishX.toDp() },
                        y = with(density) { gameState.fishY.toDp() }
                    )
            )
        }

        // UI Overlay
        GameUI(gameState = gameState)

        // Game Over Screen
        if (gameState.isGameOver) {
        }
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
    if (!gameState.initialized) return

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

fun updateGameState(currentState: GameState, context: Context, navController: NavHostController, deltaTime: Float): GameState {
    if (currentState.isGameOver || !currentState.initialized) return currentState

    val fishWidth = currentState.canvasWidth / 8
    val fishHeight = currentState.canvasHeight / 14
    val minFishY = 20f
    val maxFishY = currentState.canvasHeight - fishHeight-20f

    // Update fish position with gravity
    var newVelocity = currentState.fishVelocity + currentState.difficulty.gravity
    var newFishY = currentState.fishY + newVelocity

    // Clamp fish position within screen bounds
    if (newFishY < minFishY) {
        newFishY = minFishY
        newVelocity = 0f // Stop velocity at the ceiling
    }
    if (newFishY > maxFishY) {
        newFishY = maxFishY
        newVelocity = 0f // Stop velocity at the floor
    }


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

    val ballRespawnAreaY = fishHeight

    // Yellow ball logic
    if (hitBallCheck(currentState.fishX, newFishY, fishWidth, fishHeight, newYellowX, newYellowY)) {
        newScore += 10
        newYellowX = -100f
    }
    if (newYellowX < -fishWidth) { // Check if fully off-screen
        newYellowX = currentState.canvasWidth + Random.nextFloat() * 200f
        newYellowY = ballRespawnAreaY + Random.nextFloat() * (maxFishY - ballRespawnAreaY * 2)
    }

    // Green ball logic
    if (hitBallCheck(currentState.fishX, newFishY, fishWidth, fishHeight, newGreenX, newGreenY)) {
        newScore += 25
        newGreenX = -120f
    }
    if (newGreenX < -fishWidth) { // Check if fully off-screen
        newGreenX = currentState.canvasWidth + Random.nextFloat() * 200f
        newGreenY = ballRespawnAreaY + Random.nextFloat() * (maxFishY - ballRespawnAreaY * 2)
    }

    // Red ball logic
    if (hitBallCheck(currentState.fishX, newFishY, fishWidth, fishHeight, newRedX, newRedY)) {
        newRedX = -100f
        newScore -= 20
        newLifeCounter--

        if (newLifeCounter <= 0 && !newIsGameOver) {
            newIsGameOver = true

            // Save high score
            val prefs = context.getSharedPreferences("gameData", Context.MODE_PRIVATE)
            val highScore = prefs.getInt("highscore_${currentState.difficulty.name}", 0)

            val finalScore = if (newScore < 0) 0 else newScore

            if (finalScore > highScore) {
                prefs.edit().putInt("highscore_${currentState.difficulty.name}", finalScore).apply()
            }
            Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT).show()
            navController.navigate("game_over/${finalScore}/${currentState.difficulty.name}")
        }
    }
    if (newRedX < -fishWidth) { // Check if fully off-screen
        newRedX = currentState.canvasWidth + Random.nextFloat() * 300f
        newRedY = ballRespawnAreaY + Random.nextFloat() * (maxFishY - ballRespawnAreaY * 2)
    }

    return currentState.copy(
        fishY = newFishY,
        fishVelocity = newVelocity,
        yellowX = newYellowX,
        yellowY = newYellowY,
        greenX = newGreenX,
        greenY = newGreenY,
        redX = newRedX,
        redY = newRedY,
        score = newScore,
        lifeCounter = newLifeCounter,
        isGameOver = newIsGameOver
    )
}

fun hitBallCheck(fishX: Float, fishY: Float, fishWidth: Float, fishHeight: Float, ballX: Float, ballY: Float): Boolean {
    // collision detection
    val fishRight = fishX + fishWidth
    val fishBottom = fishY + fishHeight
    // A simplified radius for the ball for collision checking
    val ballRadius = fishWidth * 0.4f

    return fishX < ballX + ballRadius && fishRight > ballX - ballRadius &&
            fishY < ballY + ballRadius && fishBottom > ballY - ballRadius
}

@Composable
fun GameScreen(navController: NavHostController, difficulty: String) {
    FlyingFishGame(navController, difficulty)
}
