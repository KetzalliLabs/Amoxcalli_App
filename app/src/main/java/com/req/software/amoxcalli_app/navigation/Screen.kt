package com.req.software.amoxcalli_app.navigation

/**
 * Screen destinations for navigation
 */
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object MainMenu : Screen("main_menu")
    object Lesson : Screen("lesson/{lessonId}") {
        fun createRoute(lessonId: String) = "lesson/$lessonId"
    }
    object Quests : Screen("quests")
    object Profile : Screen("profile")
    object Shop : Screen("shop")
    object Library : Screen("library")
}
