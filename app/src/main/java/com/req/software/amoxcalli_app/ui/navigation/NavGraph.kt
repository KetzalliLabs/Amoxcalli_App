package com.req.software.amoxcalli_app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.req.software.amoxcalli_app.ui.components.navigation.BottomNavBar
import com.req.software.amoxcalli_app.ui.navigation.Screen.Quiz
import com.req.software.amoxcalli_app.ui.screens.home.HomeScreen
import com.req.software.amoxcalli_app.viewmodel.HomeViewModel
import com.req.software.amoxcalli_app.ui.screens.exercises.LearnGameScreen
import com.req.software.amoxcalli_app.ui.screens.exercises.LearnGameUiState
import com.req.software.amoxcalli_app.ui.screens.exercises.LearnOptionUi
import com.req.software.amoxcalli_app.ui.screens.exercises.LearnQuestionType
import com.req.software.amoxcalli_app.ui.screens.library.LibraryScreen
import com.req.software.amoxcalli_app.ui.screens.library.LibraryWordUi

/**
 * Sealed class para definir las rutas de navegación
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Topics : Screen("topics")
    object Quiz : Screen("quiz")
    object Practice : Screen("practice")
    object Profile : Screen("profile")
    object TopicDetail : Screen("topic/{topicId}") {
        fun createRoute(topicId: String) = "topic/$topicId"
    }
}

/**
 * Configuración básica de navegación
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    val homeViewModel: HomeViewModel = viewModel()

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            // Solo mostrar el bottom nav en las pantallas principales
            if (currentRoute in listOf(
                    Screen.Home.route,
                    Screen.Topics.route,
                    Quiz.route,
                    Screen.Profile.route
                )
            ) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Evitar múltiples copias de la misma pantalla
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // -------------------------------------------------------------
            // HOME
            // -------------------------------------------------------------
            composable(Screen.Home.route) {
                val userStats by homeViewModel.userStats.collectAsState()
                val recentTopics by homeViewModel.recentTopics.collectAsState()
                val recommendedTopics by homeViewModel.recommendedTopics.collectAsState()

                HomeScreen(
                    userStats = userStats,
                    recentTopics = recentTopics,
                    recommendedTopics = recommendedTopics,
                    onTopicClick = { topic ->
                        homeViewModel.navigateToTopic(topic)
                    },
                    onQuizClick = {
                        navController.navigate(Quiz.route)
                    },
                    onPracticeClick = {
                        navController.navigate(Screen.Practice.route)
                    }
                )
            }

            // -------------------------------------------------------------
            // QUIZ – Juego Aprender con tipo ALEATORIO en cada Confirmar
            // -------------------------------------------------------------
            composable(Quiz.route) {
                // Estado completo de la pregunta actual
                var currentQuestion by remember {
                    mutableStateOf(generateRandomQuestion())
                }

                LearnGameScreen(
                    uiState = currentQuestion,
                    onOptionSelected = { id ->
                        // Solo actualizamos la opción seleccionada
                        currentQuestion = currentQuestion.copy(selectedOptionId = id)
                    },
                    onConfirmClick = {
                        // Cada vez que se confirma → nueva pregunta aleatoria
                        currentQuestion = generateRandomQuestion()
                    },
                    onCloseClick = {
                        navController.popBackStack()
                    }
                )
            }

            // -------------------------------------------------------------
            // PRACTICE – puedes dejarla fija o cambiarla después
            // -------------------------------------------------------------
            composable(Screen.Practice.route) {
                var selectedOptionId by remember { mutableStateOf<String?>(null) }

                LearnGameScreen(
                    uiState = LearnGameUiState(
                        levelNumber = 5,
                        topicName = "Vehículos",
                        coins = 300,
                        energy = 20,
                        xp = 2500,
                        questionType = LearnQuestionType.WORD_TO_SIGN,
                        targetWord = "caballo",
                        options = listOf(
                            LearnOptionUi("1", "Opción 1"),
                            LearnOptionUi("2", "Opción 2"),
                            LearnOptionUi("3", "Opción 3"),
                            LearnOptionUi("4", "Opción 4")
                        ),
                        selectedOptionId = selectedOptionId
                    ),
                    onOptionSelected = { id ->
                        selectedOptionId = id
                    },
                    onConfirmClick = {
                        // Aquí podrías poner lógica distinta de práctica
                    },
                    onCloseClick = {
                        navController.popBackStack()
                    }
                )
            }


            // -------------------------------------------------------------
            // LIBRARY (por implementar)
            // -------------------------------------------------------------
            composable(Screen.Topics.route) {
                // TODO: Implementar TopicsScreen
                val sampleWordList = listOf(
                    LibraryWordUi("1", "Carro", true),
                    LibraryWordUi("2", "Avión"),
                    LibraryWordUi("3", "Camión"),
                    LibraryWordUi("4", "Bicicleta"),
                    LibraryWordUi("5", "Tren"),
                    LibraryWordUi("6", "Moto", true),
                )
                LibraryScreen(
                    words = sampleWordList,
                    onWordClick = { wordId ->
                        navController.navigate("wordDetail/$wordId")
                    }
                )
            }

            // -------------------------------------------------------------
            // PROFILE (por implementar) pene
            // -------------------------------------------------------------
            composable(Screen.Profile.route) {
                // TODO: Implementar ProfileScreen
            }
        }
    }
}

/**
 * Genera una pregunta aleatoria de uno de los 3 tipos:
 *  - VIDEO_TO_TEXT
 *  - IMAGE_TO_TEXT
 *  - WORD_TO_SIGN
 */
private fun generateRandomQuestion(): LearnGameUiState {
    val type = LearnQuestionType.values().random()

    return when (type) {
        LearnQuestionType.VIDEO_TO_TEXT -> {
            LearnGameUiState(
                levelNumber = 5,
                topicName = "Vehículos",
                energy = 20,
                questionType = LearnQuestionType.VIDEO_TO_TEXT,
                promptText = "¿Cuál es esta palabra?",
                options = listOf(
                    LearnOptionUi("1", "Carro"),
                    LearnOptionUi("2", "Avión"),
                    LearnOptionUi("3", "Camión"),
                    LearnOptionUi("4", "Bicicleta")
                ),
                selectedOptionId = null
            )
        }

        LearnQuestionType.IMAGE_TO_TEXT -> {
            LearnGameUiState(
                levelNumber = 5,
                topicName = "Vehículos",
                energy = 20,
                questionType = LearnQuestionType.IMAGE_TO_TEXT,
                promptText = "¿Cuál es esta palabra?",
                options = listOf(
                    LearnOptionUi("1", "Carro"),
                    LearnOptionUi("2", "Camión"),
                    LearnOptionUi("3", "Bicicleta"),
                    LearnOptionUi("4", "Avión")
                ),
                selectedOptionId = null
            )
        }

        LearnQuestionType.WORD_TO_SIGN -> {
            LearnGameUiState(
                levelNumber = 5,
                topicName = "Vehículos",
                coins = 300,
                energy = 20,
                xp = 2500,
                questionType = LearnQuestionType.WORD_TO_SIGN,
                targetWord = "caballo",
                options = listOf(
                    LearnOptionUi("1", "Seña A"),
                    LearnOptionUi("2", "Seña B"),
                    LearnOptionUi("3", "Seña C"),
                    LearnOptionUi("4", "Seña D")
                ),
                selectedOptionId = null
            )
        }
    }
}
