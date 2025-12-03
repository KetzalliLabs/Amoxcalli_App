package com.req.software.amoxcalli_app.ui.screens.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.req.software.amoxcalli_app.data.dto.UserStatsResponse
import com.req.software.amoxcalli_app.viewmodel.ExerciseViewModel
import com.req.software.amoxcalli_app.viewmodel.SessionState
// Imports   MediaDisplay
import com.req.software.amoxcalli_app.ui.screens.exercises.common.MediaDisplay
import com.req.software.amoxcalli_app.ui.screens.exercises.common.MediaType
import com.req.software.amoxcalli_app.ui.theme.ThirdColor

/**
 * Exercise screen that fetches data from backend API
 * Uses ExerciseViewModel to handle exercise logic
 * 
 * @param categoryId Optional category ID to filter exercises by category
 */
@Composable
fun ApiExerciseScreen(
    userStats: UserStatsResponse?,
    authToken: String?,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
    categoryId: String? = null,
    exerciseViewModel: ExerciseViewModel = viewModel(),
    userStatsViewModel: com.req.software.amoxcalli_app.viewmodel.UserStatsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val currentExercise by exerciseViewModel.currentExercise.collectAsState()
    val isLoading by exerciseViewModel.isLoading.collectAsState()
    val error by exerciseViewModel.error.collectAsState()
    val selectedAnswerId by exerciseViewModel.selectedAnswerId.collectAsState()
    val answerResult by exerciseViewModel.answerResult.collectAsState()
    val correctCount by exerciseViewModel.correctCount.collectAsState()
    val wrongCount by exerciseViewModel.wrongCount.collectAsState()
    val sessionState by exerciseViewModel.sessionState.collectAsState()

    // Get reactive user stats for updated XP display
    val currentUserStats by userStatsViewModel.userStats.collectAsState()

    // Get local XP for real-time updates
    val localXP by userStatsViewModel.localXP.collectAsState()

    // Load exercises when screen starts (filtered by category if provided)
    LaunchedEffect(categoryId) {
        if (categoryId != null) {
            exerciseViewModel.loadExercises(categoryId)
        } else {
            exerciseViewModel.loadRandomExercise()
        }
    }

    // Show game over screen if session ended
    if (sessionState != SessionState.ACTIVE) {
        GameOverScreen(
            sessionState = sessionState,
            correctCount = correctCount,
            wrongCount = wrongCount,
            onRestart = {
                exerciseViewModel.reset()
                if (categoryId != null) {
                    exerciseViewModel.loadExercises(categoryId)
                } else {
                    exerciseViewModel.loadRandomExercise()
                }
            },
            onExit = onCloseClick
        )
        return
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            error != null -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = error ?: "Error desconocido",
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    androidx.compose.material3.Button(
                        onClick = {
                            if (categoryId != null) {
                                exerciseViewModel.loadExercises(categoryId)
                            } else {
                                exerciseViewModel.loadRandomExercise()
                            }
                        }
                    ) {
                        Text("Reintentar")
                    }
                }
            }
            currentExercise != null -> {
                val exercise = currentExercise!!

                // Extract user stats - use local XP for real-time updates
                val coins = currentUserStats?.stats?.find { it.name == "coins" }?.currentValue ?: 0
                val energy = currentUserStats?.stats?.find { it.name == "energy" }?.currentValue ?: 20
                // Use local XP instead of API XP for instant updates
                val xp = localXP

                val mediaType = when {
                    !exercise.correctSign.videoUrl.isNullOrBlank() -> MediaType.VIDEO
                    !exercise.correctSign.imageUrl.isNullOrBlank() -> MediaType.IMAGE
                    else -> MediaType.NONE
                }

                // Determine question type based on video/image availability
                val questionType = when {
                    exercise.correctSign.videoUrl != null -> LearnQuestionType.VIDEO_TO_TEXT
                    exercise.correctSign.imageUrl != null -> LearnQuestionType.IMAGE_TO_TEXT
                    else -> LearnQuestionType.WORD_TO_SIGN
                }

                // Map exercise options to LearnOptionUi
                val uiOptions = exercise.options.map { option ->
                    LearnOptionUi(
                        id = option.id,
                        text = option.name
                    )
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    // Progress indicator
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1976D2))
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "✓ Correctas: $correctCount/${ExerciseViewModel.MAX_CORRECT_ANSWERS}",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "✗ Incorrectas: $wrongCount/${ExerciseViewModel.MAX_WRONG_ANSWERS}",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Create UI state
                    val uiState = LearnGameUiState(
                        levelNumber = 5,
                        topicName = exercise.categoryName,
                        coins = coins,
                        energy = energy,
                        xp = xp,
                        questionType = questionType,
                        promptText = exercise.question,
                        targetWord = exercise.correctSign.name,
                        options = uiOptions,
                        selectedOptionId = selectedAnswerId
                    )

                    LearnGameScreen(
                        uiState = uiState,
                        // Le pasamos la información del medio
                        mediaType = mediaType,
                        videoUrl = exercise.correctSign.videoUrl,
                        imageUrl = exercise.correctSign.imageUrl,
                        // El resto de los parámetros no cambian
                        onOptionSelected = { optionId ->
                            exerciseViewModel.selectAnswer(optionId)
                        },
                        onConfirmClick = {
                            if (answerResult == null) {
                                exerciseViewModel.checkAnswer(authToken) {
                                    // Award XP for correct answer
                                    userStatsViewModel.awardCorrectAnswerXP()
                                }
                            } else {
                                if (categoryId != null) {
                                    exerciseViewModel.nextExercise()
                                } else {
                                    exerciseViewModel.loadRandomExercise()
                                }
                            }
                        },
                        onCloseClick = onCloseClick
                    )
                }

                // Show answer result overlay with improved visibility
                answerResult?.let { result ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 120.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (result.isCorrect)
                                    Color(0xFF4CAF50)
                                else
                                    Color(0xFFF44336)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = result.message,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Game Over Screen shown when session ends
 */
@Composable
fun GameOverScreen(
    sessionState: SessionState,
    correctCount: Int,
    wrongCount: Int,
    onRestart: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xF0000000)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = when (sessionState) {
                        SessionState.WON -> "¡Felicidades!"
                        SessionState.LOST -> "Juego Terminado"
                        else -> ""
                    },
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (sessionState) {
                        SessionState.WON -> Color(0xFF4CAF50)
                        SessionState.LOST -> Color(0xFFF44336)
                        else -> Color.Black
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Message
                Text(
                    text = when (sessionState) {
                        SessionState.WON -> "¡Completaste el desafío!"
                        SessionState.LOST -> "Alcanzaste el límite de errores"
                        else -> ""
                    },
                    fontSize = 18.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = correctCount.toString(),
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                        Text(
                            text = "Correctas",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = wrongCount.toString(),
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF44336)
                        )
                        Text(
                            text = "Incorrectas",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Buttons
                Button(
                    onClick = onRestart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1976D2)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Jugar de Nuevo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onExit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Salir",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
