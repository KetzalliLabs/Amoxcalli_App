package com.req.software.amoxcalli_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.req.software.amoxcalli_app.ui.navigation.AppNavigation
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
import com.req.software.amoxcalli_app.viewmodel.UserStatsViewModel
import com.req.software.amoxcalli_app.ui.navigation.AppNavigation

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
    val userStatsViewModel: UserStatsViewModel = viewModel()
    val currentUser by authViewModel.currentUser.collectAsState()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (currentUser != null) "app_navigation" else Screen.Login.route
    ) {
        // Login Screen
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                userStatsViewModel = userStatsViewModel,
                onLoginSuccess = {
                    navController.navigate("app_navigation") {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Main App Navigation (Home, Learn, Library, Quiz, Profile)
        composable("app_navigation") {
            AppNavigation(
                authViewModel = authViewModel,
                onLogout = {
                    userStatsViewModel.clearStats()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Legacy routes maintained for backward compatibility
        // Main Menu Screen (old Duolingo-style learning path)
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