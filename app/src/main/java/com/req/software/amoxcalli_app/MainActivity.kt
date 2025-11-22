package com.req.software.amoxcalli_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.req.software.amoxcalli_app.navigation.Screen
import com.req.software.amoxcalli_app.ui.screens.*
import com.req.software.amoxcalli_app.ui.theme.Amoxcalli_AppTheme
import com.req.software.amoxcalli_app.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Amoxcalli_AppTheme {
                AmoxcalliApp()
            }
        }
    }
}

@Composable
fun AmoxcalliApp() {
    val authViewModel: AuthViewModel = viewModel()
    val currentUser by authViewModel.currentUser.collectAsState()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (currentUser != null) Screen.MainMenu.route else Screen.Login.route
    ) {
        // Login Screen
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.MainMenu.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Main Menu Screen
        composable(Screen.MainMenu.route) {
            val userId = currentUser?.id ?: currentUser?.firebaseUid ?: "default_user"
            MainMenuScreen(
                userId = userId,
                currentRoute = Screen.MainMenu.route,
                onNavigateToLesson = { lessonId ->
                    navController.navigate(Screen.Lesson.createRoute(lessonId))
                },
                onNavigateToRoute = { route ->
                    navController.navigate(route) {
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                    }
                }
            )
        }

        // Quests Screen
        composable(Screen.Quests.route) {
            QuestsScreen(
                currentRoute = Screen.Quests.route,
                onNavigateToRoute = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // Profile Screen
        composable(Screen.Profile.route) {
            ProfileScreen(
                currentRoute = Screen.Profile.route,
                onNavigateToRoute = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // Shop Screen
        composable(Screen.Shop.route) {
            ShopScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Lesson Screen
        composable(
            route = Screen.Lesson.route,
            arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
            LessonScreen(
                lessonId = lessonId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}