package com.req.software.amoxcalli_app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.req.software.amoxcalli_app.ui.components.navigation.BottomNavBar
import com.req.software.amoxcalli_app.ui.screens.home.HomeScreen
import com.req.software.amoxcalli_app.viewmodel.HomeViewModel

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
                Screen.Quiz.route,
                Screen.Profile.route
            )) {
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
            composable(Screen.Home.route) {
                val userStats by homeViewModel.userStats.collectAsState()
                val recentTopics by homeViewModel.recentTopics.collectAsState()
                val recommendedTopics by homeViewModel.recommendedTopics.collectAsState()
                
                HomeScreen(
                    userStats = userStats,
                    recentTopics = recentTopics,
                    recommendedTopics = recommendedTopics,
                    onTopicClick = { topic -> homeViewModel.navigateToTopic(topic) },
                    onQuizClick = { homeViewModel.startDailyQuiz() },
                    onPracticeClick = { homeViewModel.startPractice() }
                )
            }
            
            composable(Screen.Topics.route) {
                // TODO: Implementar TopicsScreen
            }
            
            composable(Screen.Quiz.route) {
                // TODO: Implementar QuizScreen
            }
            
            composable(Screen.Profile.route) {
                // TODO: Implementar ProfileScreen
            }
        }
    }
}