package com.req.software.amoxcalli_app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
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
import com.req.software.amoxcalli_app.ui.screens.exercises.ApiExerciseScreen
import com.req.software.amoxcalli_app.ui.screens.library.LibraryScreen
import com.req.software.amoxcalli_app.ui.screens.profile.ProfileScreen
import com.req.software.amoxcalli_app.ui.screens.categories.CategoriesScreen
import com.req.software.amoxcalli_app.ui.screens.categories.CategoryDetailScreen
import com.req.software.amoxcalli_app.ui.screens.favorites.FavoritesScreen
import com.req.software.amoxcalli_app.ui.screens.admin.AdminScreen
import com.req.software.amoxcalli_app.viewmodel.AuthViewModel
import com.req.software.amoxcalli_app.viewmodel.UserStatsViewModel

/**
 * Sealed class para definir las rutas de navegación
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Learn : Screen("topics")
    object Topics : Screen("learn")
    object Quiz : Screen("quiz")
    object Practice : Screen("practice")
    object Profile : Screen("profile")
    object Exercises : Screen("exercises")
    object Categories : Screen("categories")
    object Favorites : Screen("favorites")
    object Admin : Screen("admin")
    object CategoryDetail : Screen("category/{categoryId}") {
        fun createRoute(categoryId: String) = "category/$categoryId"
    }
    object TopicDetail : Screen("topic/{topicId}") {
        fun createRoute(topicId: String) = "topic/$topicId"
    }
    object WordDetail : Screen("wordDetail/{wordId}") {
        fun createRoute(wordId: String) = "wordDetail/$wordId"
    }
}

/**
 * Configuración básica de navegación
 */
@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel(),
    onLogout: () -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    val homeViewModel: HomeViewModel = viewModel()
    val userStatsViewModel: UserStatsViewModel = viewModel()

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            // Only show bottom nav on main screens
            if (currentRoute in listOf(
                    Screen.Home.route,
                    Screen.Topics.route,
                    Screen.Categories.route,
                    Screen.Favorites.route,
                    Screen.Profile.route
                )
            ) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Avoid multiple copies of the same screen
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
            // HOME - Simplified
            // -------------------------------------------------------------
            composable(Screen.Home.route) {
                val apiUserStats by userStatsViewModel.userStats.collectAsState()
                val userStats by homeViewModel.userStats.collectAsState()
                val medals by homeViewModel.medals.collectAsState()

                // Update HomeViewModel with data from UserStatsViewModel
                LaunchedEffect(apiUserStats) {
                    homeViewModel.updateUserStats(apiUserStats)
                }

                HomeScreen(
                    userStats = userStats,
                    medals = medals,
                    onQuizClick = {
                        navController.navigate(Quiz.route)
                    },
                    onPracticeClick = {
                        navController.navigate(Screen.Practice.route)
                    },
                    onLibraryClick = {
                        navController.navigate(Screen.Topics.route)
                    }
                )
            }

            // REMOVED: Learn section - app simplified
            // Learn route removed as per app simplification


            // -------------------------------------------------------------
            // QUIZ – Daily Quiz (10 questions from categories)
            // -------------------------------------------------------------
            composable(Quiz.route) {
                val userStats by userStatsViewModel.userStats.collectAsState()
                val authToken by authViewModel.authToken.collectAsState()

                // TODO: Replace with DailyQuizScreen once implemented
                ApiExerciseScreen(
                    userStats = userStats,
                    authToken = authToken,
                    onCloseClick = {
                        navController.popBackStack()
                    }
                )
            }

            // -------------------------------------------------------------
            // PRACTICE – Word Practice (from exercises endpoint)
            // -------------------------------------------------------------
            composable(Screen.Practice.route) {
                val userStats by userStatsViewModel.userStats.collectAsState()
                val authToken by authViewModel.authToken.collectAsState()

                ApiExerciseScreen(
                    userStats = userStats,
                    authToken = authToken,
                    onCloseClick = {
                        navController.popBackStack()
                    }
                )
            }

            // -------------------------------------------------------------
            // LIBRARY / TOPICS
            // -------------------------------------------------------------
            composable(Screen.Topics.route) {
                val userStats by userStatsViewModel.userStats.collectAsState()
                val authToken by authViewModel.authToken.collectAsState()

                LibraryScreen(
                    userStats = userStats,
                    authToken = authToken,
                    onWordClick = { wordId -> navController.navigate("wordDetail/$wordId") }
                )
            }

            // -------------------------------------------------------------
            // CATEGORIES – Pantalla de categorías
            // -------------------------------------------------------------
            composable(Screen.Categories.route) {
                val userStats by userStatsViewModel.userStats.collectAsState()

                CategoriesScreen(
                    userStats = userStats,
                    onCategoryClick = { categoryId ->
                        navController.navigate(Screen.CategoryDetail.createRoute(categoryId))
                    }
                )
            }

            // -------------------------------------------------------------
            // FAVORITES – Pantalla de favoritos
            // -------------------------------------------------------------
            composable(Screen.Favorites.route) {
                val userStats by userStatsViewModel.userStats.collectAsState()

                FavoritesScreen(
                    userStats = userStats,
                    onWordClick = { wordId ->
                        navController.navigate(Screen.WordDetail.createRoute(wordId))
                    }
                )
            }

            // -------------------------------------------------------------
            // CATEGORY DETAIL – Señas filtradas por categoría
            // -------------------------------------------------------------
            composable(Screen.CategoryDetail.route) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: return@composable
                val userStats by userStatsViewModel.userStats.collectAsState()
                val authToken by authViewModel.authToken.collectAsState()

                CategoryDetailScreen(
                    categoryId = categoryId,
                    userStats = userStats,
                    authToken = authToken,
                    onWordClick = { wordId ->
                        navController.navigate(Screen.WordDetail.createRoute(wordId))
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            // OLD HARDCODED VERSION (REMOVED)
            /*
            composable(Screen.Topics.route) {
                val userStats by homeViewModel.userStats.collectAsState()

                // Example JSON (replace with network / asset loading in real app)
                val sampleWordsJson = """
    {
    "success": true,
    "count": 93,
    "data": [
        {
            "id": "396f25c7-1c08-41a5-8fa8-5a8b3fbe4e7f",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "A",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/a.JPG",
            "video_url": null
        },
        {
            "id": "85280f5f-a8cb-494e-b919-263740c4ce7e",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Abeja",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Abeja_Web.m4v"
        },
        {
            "id": "b32e1c95-6bac-4854-ac97-c9560045dabc",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Aceite",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Aceite_Web.m4v"
        },
        {
            "id": "710291af-6195-4a18-a18e-451cc366bf79",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Água",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Agua_Web.m4v"
        },
        {
            "id": "fbd5e770-c71e-41d2-9a2c-dee7eb667e84",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Águila",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Aguila_Web.m4v"
        },
        {
            "id": "8ebef5a5-2ba9-4b14-a3ee-f1c528ca2a0d",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Araña",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Arana_Web.m4v"
        },
        {
            "id": "88ec9c1b-37c1-4a64-bd0f-1ee996152bca",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Ardilla",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Ardilla_Web.m4v"
        },
        {
            "id": "2a680dd8-3a98-4e40-972c-bd58a39bb7ad",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Arroz",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Arroz_Web.m4v"
        },
        {
            "id": "8f87de79-89f7-41c3-ac82-b59338711e0c",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Azúcar",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Azucar_Web.m4v"
        },
        {
            "id": "b52956e1-1fad-41c5-aea3-2ef8e702dab7",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "B",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/b.JPG",
            "video_url": null
        },
        {
            "id": "d1f50db4-1aff-46ca-9d17-2a2f5e662347",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Burro",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Burro_Web.m4v"
        },
        {
            "id": "f713628c-e40d-4966-b959-66cc34903ab7",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "C",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/c.JPG",
            "video_url": null
        },
        {
            "id": "8b7b9021-7240-4859-80b0-48a0f0433715",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Caballo",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Caballo_Web.m4v"
        },
        {
            "id": "af9a61d7-633f-4ee0-9847-e8d6d61a6897",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Café",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Cafe_Web.m4v"
        },
        {
            "id": "338da219-7195-42f7-9c49-a5d857df07bd",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Caldo",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Caldo_Web.m4v"
        },
        {
            "id": "206d9dd4-fd76-4b05-b608-f993a154cd71",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Carne",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Carne_Web.m4v"
        },
        {
            "id": "0cb7443e-38a6-469f-8537-baf6367e7950",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Cerdo",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Cerdo_Web.m4v"
        },
        {
            "id": "aa445bef-49d8-41a4-9def-7803cc746fdb",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Cerveza",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Cerveza_Web.m4v"
        },
        {
            "id": "b123b0d1-ecb7-4031-9adc-508471b5a2ad",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Chango",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Chango_Web.m4v"
        },
        {
            "id": "fd91454d-98fe-4670-9f6f-0472c0d35bbc",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Chile",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Chile_Web.m4v"
        },
        {
            "id": "8069c8a2-acdb-42fa-b50d-0685c59047aa",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Chocolate",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Chocolate_Web.m4v"
        },
        {
            "id": "e78064f6-5efb-4ebe-8aae-0e281f4cb2a7",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Coca Cola",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Cocacola_Web.m4v"
        },
        {
            "id": "2358e0f3-7c74-4cc2-be0e-7331a3511ae3",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Conejo",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Conejo_Web.m4v"
        },
        {
            "id": "d369ca45-6c1e-471c-92a5-480ca22bc836",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "D",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/d.JPG",
            "video_url": null
        },
        {
            "id": "2fdf1781-45ad-4433-9d29-8355da2a32e4",
            "category_id": "2706287e-c01f-4e7c-8bfb-d53ac6118a78",
            "name": "Domingo",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Domingo_Web.m4v"
        },
        {
            "id": "a1c53e27-488f-4281-8249-87f11818ef74",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Dulce",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Dulce_Web.m4v"
        },
        {
            "id": "2ce32528-87fb-4c0b-a913-0315a62cb2c3",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "E",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/e.JPG",
            "video_url": null
        },
        {
            "id": "3fa375e9-650c-4566-8747-5e0e0a07949d",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Ensalada",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Ensalada_Web.m4v"
        },
        {
            "id": "20d8a375-1188-40f1-aa3e-5b71d4c25dbb",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "F",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/f.JPG",
            "video_url": null
        },
        {
            "id": "4e1f208e-6707-450b-8f1d-c9c021abba05",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Frijol",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Frijol_Web.m4v"
        },
        {
            "id": "665d6cdf-2d9a-49d8-8509-a9f01f452777",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Fruta",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Fruta_Web.m4v"
        },
        {
            "id": "a52b4c0a-8ae7-4830-a5e7-d7ced53c6db3",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "G",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/g.JPG",
            "video_url": null
        },
        {
            "id": "5fa9a9c0-1e80-4862-b0fb-e92dd2bf02d7",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Galleta",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Galleta_Web.m4v"
        },
        {
            "id": "0f1242a8-0e88-4c46-b42b-81a209e222d8",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Gato",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Gato_Web.m4v"
        },
        {
            "id": "4254da41-da49-4b22-b729-c690184299f3",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Gorila",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Gorila_Web.m4v"
        },
        {
            "id": "d008ed4d-a559-4e93-a6e7-20c431e05e53",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Gusano",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Gusano_Web.m4v"
        },
        {
            "id": "17c8abc2-de40-4b31-8954-2ccca5425763",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "H",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/h.JPG",
            "video_url": null
        },
        {
            "id": "2039dab8-296c-46bf-b3b9-77d38df6b29e",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Hamburguesa",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Hamburguesa_Web.m4v"
        },
        {
            "id": "dca219df-5446-4464-b143-30460bed1cf7",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Huevo",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Huevo_Web.m4v"
        },
        {
            "id": "02499204-4319-4ee5-aff5-c3b326805276",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "I",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/i.JPG",
            "video_url": null
        },
        {
            "id": "c53a0ac2-48b4-4afd-b746-0eda911cb6f8",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "J",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/J_Web.m4v"
        },
        {
            "id": "48033801-107c-4cc0-affd-52ae961dae81",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Jirafa",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Jirafa_Web.m4v"
        },
        {
            "id": "9cc00460-8aef-463b-953f-2ec1a9cf7356",
            "category_id": "2706287e-c01f-4e7c-8bfb-d53ac6118a78",
            "name": "Jueves",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Jueves_Web.m4v"
        },
        {
            "id": "0fc19f82-1f19-490d-848d-010bc1d8f96b",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "K",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/k_Web.m4v"
        },
        {
            "id": "6d9bbfbc-f439-4801-9d4a-77d1f922af58",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "L",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/l.JPG",
            "video_url": null
        },
        {
            "id": "92bab159-c42a-4b09-ba0f-39b90d29a7ec",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Leche",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Leche_Web.m4v"
        },
        {
            "id": "f829272e-7440-4733-ad75-45ab0831648e",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "León",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Leon_Web.m4v"
        },
        {
            "id": "1d402420-2194-4f67-8c2a-b96996fcebf9",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "Ll",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Ll_Web.m4v"
        },
        {
            "id": "83f3f6bf-c496-4254-bc91-b34e138ce33d",
            "category_id": "2706287e-c01f-4e7c-8bfb-d53ac6118a78",
            "name": "Lunes",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Lunes_Web.m4v"
        },
        {
            "id": "5e748293-f0fc-4b92-a757-01549bf1a64d",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "M",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/m.JPG",
            "video_url": null
        },
        {
            "id": "c86a7e39-53af-47f6-94d3-20da7a9ec7a3",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Mariposa",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Mariposa_Web.m4v"
        },
        {
            "id": "cf25be89-f4e6-4d8e-bcaf-cd72eee530f0",
            "category_id": "2706287e-c01f-4e7c-8bfb-d53ac6118a78",
            "name": "Martes",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Martes_Web.m4v"
        },
        {
            "id": "6275e758-078a-465d-81dc-8c8281588a0f",
            "category_id": "2706287e-c01f-4e7c-8bfb-d53ac6118a78",
            "name": "Miércoles",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Miercoles_Web.m4v"
        },
        {
            "id": "77188cdd-fe89-4fa5-81ff-054fd614ddd2",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "N",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/n.JPG",
            "video_url": null
        },
        {
            "id": "b0f5d903-b660-467c-a7dc-4741bfd32a8b",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "Ñ",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Nie_Web.m4v"
        },
        {
            "id": "63f0eca3-fbc6-44ab-9809-930705d18579",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "O",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/o.JPG",
            "video_url": null
        },
        {
            "id": "ff561cfe-9e91-4932-954c-e52e6a9d0f58",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Oso",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Oso_Web.m4v"
        },
        {
            "id": "f67ed488-545e-4bdd-b931-d31d8e42e8c0",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "P",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/p.JPG",
            "video_url": null
        },
        {
            "id": "68e46777-83bc-4dc9-8be3-20152c4f9032",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Pajaro",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Pajaro_Web.m4v"
        },
        {
            "id": "21811b4e-3cad-43fb-a640-71d088c0632e",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Paloma",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Paloma_Web.m4v"
        },
        {
            "id": "65567b5f-1943-4e4e-a4dd-d88887f35567",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Pan",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Pan_Web.m4v"
        },
        {
            "id": "82794486-1557-474c-acc3-65ecd03793d4",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Pastel",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Pastel_Web.m4v"
        },
        {
            "id": "1946ff39-cb91-40e7-a514-9e61522e3391",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Pato",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Pato_Web.m4v"
        },
        {
            "id": "7ce053d0-89c8-4ff0-bc4e-a87e71708425",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Perro",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Perro_Web.m4v"
        },
        {
            "id": "3af96996-3d0f-4f01-8d85-94dab20e1c9d",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Pescado",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Pescado_Web.m4v"
        },
        {
            "id": "ebfcb6a4-4b59-46de-847e-91fbf630c684",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Pez",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Pez_Web.m4v"
        },
        {
            "id": "913cca14-5bc5-4fe5-8b2e-5e42794c3d8d",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Pizza",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Pizza_Web.m4v"
        },
        {
            "id": "db154cce-ffd3-41bf-8f44-9c1c0e93bd04",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Pollo",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Pollo_Web.m4v"
        },
        {
            "id": "a77a46d5-49d7-413e-af41-5f9ad65bda29",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "Q",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Q_Web.m4v"
        },
        {
            "id": "6d4a22b2-09b1-4d00-8933-65847b2d4ec9",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Queso",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Queso_Web.m4v"
        },
        {
            "id": "80153ee8-c14f-4683-8d2d-bdd67b9c34a6",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "R",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/r.JPG",
            "video_url": null
        },
        {
            "id": "96b70055-5603-4872-99ee-d9b6cb42a01d",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Ratón",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Raton_Web.m4v"
        },
        {
            "id": "062ffe79-8f88-483a-b34b-8b5c50d82f91",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Refresco",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Refresco_Web.m4v"
        },
        {
            "id": "2dcc960c-0a02-4486-8de4-0f4bf4de8bd2",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "Rr",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Rr_Web.m4v"
        },
        {
            "id": "ef56a85a-8ef6-48c4-84be-7d288e6e5562",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "S",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/s.JPG",
            "video_url": null
        },
        {
            "id": "8c4dadd7-7ece-4fdb-b727-0fb6f59b949a",
            "category_id": "2706287e-c01f-4e7c-8bfb-d53ac6118a78",
            "name": "Sábado",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Sabado_Web.m4v"
        },
        {
            "id": "643e63f7-17de-4379-8f82-b70a1063e86e",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Sopa",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Sopa_Web.m4v"
        },
        {
            "id": "676c70af-60bb-4758-8a82-a030ce3563ec",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "T",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/t.JPG",
            "video_url": null
        },
        {
            "id": "163ed461-9cfd-46a1-b4ca-61fa91517623",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Taco",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Taco_Web.m4v"
        },
        {
            "id": "57b099cc-09f4-44ea-a365-12178bf21c5b",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Tigre",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Tigre_Web.m4v"
        },
        {
            "id": "fc5af2ea-903e-4443-a839-14482a425d1c",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Toro",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Toro_Web.m4v"
        },
        {
            "id": "49f0e4bc-5ceb-40f3-a4c5-42462ebe4977",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Tortilla",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Tortilla_Web.m4v"
        },
        {
            "id": "f9e67e98-08c5-48b0-a863-011a694349df",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Tortuga",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Tortuga_Web.m4v"
        },
        {
            "id": "8f678e57-2ca0-41cf-9c62-ec40e1446db4",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "U",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/u.JPG",
            "video_url": null
        },
        {
            "id": "0f52f9f9-351c-4220-8add-f876ff509e80",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "V",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/v.JPG",
            "video_url": null
        },
        {
            "id": "5bfd5c9c-d403-454b-8ef2-22df7c2c0e3f",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Vaca",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Vaca_Web.m4v"
        },
        {
            "id": "23f63511-d39b-4f92-ac49-d8f9a84488e5",
            "category_id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
            "name": "Verdura",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Verdura_Web.m4v"
        },
        {
            "id": "5e27d02c-afd5-4077-96bb-8e1462f47591",
            "category_id": "1097af2e-515f-41b5-8503-3deaaa30514a",
            "name": "Vibora",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Vibora_Web.m4v"
        },
        {
            "id": "1073cbad-0014-4de2-a53f-fde347e68efb",
            "category_id": "2706287e-c01f-4e7c-8bfb-d53ac6118a78",
            "name": "Viernes",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Viernes_Web.m4v"
        },
        {
            "id": "4e38c977-c7b0-4bcc-ae96-c5fe3e31a157",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "W",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/w.JPG",
            "video_url": null
        },
        {
            "id": "4637221a-ad18-4c38-8520-83e58b218f42",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "X",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/X_Web.m4v"
        },
        {
            "id": "cba85fa8-5e62-4d2f-b1d1-fa663da05594",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "Y",
            "description": null,
            "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/y.JPG",
            "video_url": null
        },
        {
            "id": "6a3115d9-ff82-4216-be46-13cb7efd43d8",
            "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
            "name": "Z",
            "description": null,
            "image_url": null,
            "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Z_Web.m4v"
        }
    ]
}
    """.trimIndent()

                val sampleCategoriesJson = """
    {
        "success": true,
        "count": 23,
        "data": [
            {
                "id": "fb59bac7-7119-4c93-837e-f12469bf699b",
                "name": "Abecedario",
                "icon_url": "https://img.icons8.com/?size=100&id=MXyR2CZikptq&format=png&color=000000"
            },
            {
                "id": "1097af2e-515f-41b5-8503-3deaaa30514a",
                "name": "Animales",
                "icon_url": "https://img.icons8.com/?size=100&id=ZuzscBIny4qF&format=png&color=000000"
            },
            {
                "id": "e40bdeee-3573-4eee-8a19-87008dc9b43d",
                "name": "Colores",
                "icon_url": "https://img.icons8.com/?size=100&id=859&format=png&color=000000"
            },
            {
                "id": "e0bc8a70-b791-4826-ae17-2963d7382c4e",
                "name": "Comida",
                "icon_url": "https://img.icons8.com/?size=100&id=7613&format=png&color=000000"
            },
            {
                "id": "056557c6-bb23-4004-89b6-8e8997ce8827",
                "name": "Cuerpo",
                "icon_url": "https://img.icons8.com/?size=100&id=60865&format=png&color=000000"
            },
            {
                "id": "2706287e-c01f-4e7c-8bfb-d53ac6118a78",
                "name": "Dias de la Semana",
                "icon_url": "https://img.icons8.com/?size=100&id=4NXrrgLpkEuk&format=png&color=000000"
            },
            {
                "id": "8c1e45d7-4452-4eed-9fa6-d392e937b010",
                "name": "Frutas",
                "icon_url": "https://img.icons8.com/?size=100&id=GWislv927j8I&format=png&color=000000"
            },
            {
                "id": "28e0ad9f-08b1-48ad-b75b-d334b907bafe",
                "name": "Gramática",
                "icon_url": "https://img.icons8.com/?size=100&id=OzvsjhXE24ws&format=png&color=000000"
            },
            {
                "id": "7653c2bd-6d52-4a1b-b37d-b4b1b35ca2f4",
                "name": "Hogar",
                "icon_url": "https://img.icons8.com/?size=100&id=4823&format=png&color=000000"
            },
            {
                "id": "cd1c5d70-1255-4822-b105-0e112aed6e61",
                "name": "Lugares",
                "icon_url": "https://img.icons8.com/?size=100&id=undefined&format=png&color=000000"
            },
            {
                "id": "2bcd5119-b7b7-4f15-b719-fd7f623f48e5",
                "name": "Meses del Año",
                "icon_url": "https://img.icons8.com/?size=100&id=39mJ0wyUp2YO&format=png&color=000000"
            },
            {
                "id": "317e163b-f52c-480a-9f2b-e3147fca2b1e",
                "name": "Números",
                "icon_url": "https://img.icons8.com/?size=100&id=107428&format=png&color=000000"
            },
            {
                "id": "9c8f8d66-079b-4e02-b320-31b6b31fd261",
                "name": "Oficios",
                "icon_url": "https://img.icons8.com/?size=100&id=tJJWhcXCnpyE&format=png&color=000000"
            },
            {
                "id": "f4d4eed0-982d-4f58-ad3e-c47598683f18",
                "name": "Personas",
                "icon_url": "https://img.icons8.com/?size=100&id=undefined&format=png&color=000000"
            },
            {
                "id": "8c067d73-d20f-4dce-8d41-c4ebe3b21bdb",
                "name": "Preguntas",
                "icon_url": "https://img.icons8.com/?size=100&id=4501&format=png&color=000000"
            },
            {
                "id": "de332021-c7ff-4475-b8c8-0273b3715ac1",
                "name": "Pronombres",
                "icon_url": "https://img.icons8.com/?size=100&id=UzivRUTTbNKR&format=png&color=000000"
            },
            {
                "id": "401ff86a-e9cf-4a52-9585-5ebafee86eab",
                "name": "Ropa",
                "icon_url": "https://img.icons8.com/?size=100&id=24899&format=png&color=000000"
            },
            {
                "id": "d7366df0-1718-491c-b4ee-62603a167eae",
                "name": "Saludos",
                "icon_url": "https://img.icons8.com/?size=100&id=25573&format=png&color=000000"
            },
            {
                "id": "a3cf1fa9-aa92-4e2a-bfb9-eb2e9f22e12b",
                "name": "Tiempo",
                "icon_url": "https://img.icons8.com/?size=100&id=19100&format=png&color=000000"
            },
            {
                "id": "68c4b33b-9732-4cbf-83a4-de7800c345d2",
                "name": "Transporte",
                "icon_url": "https://img.icons8.com/?size=100&id=undefined&format=png&color=000000"
            },
            {
                "id": "7be74295-e290-46e8-ad5f-f67bebdf6340",
                "name": "Verbos comunes",
                "icon_url": "https://img.icons8.com/?size=100&id=ro6d8kA3GXyL&format=png&color=000000"
            },
            {
                "id": "189059e5-a153-4d12-b1f0-a7cc99b00c67",
                "name": "Verbos Narrativos",
                "icon_url": "https://img.icons8.com/?size=100&id=undefined&format=png&color=000000"
            },
            {
                "id": "f4e7ff0d-57c0-4474-ba0c-ec77bd16cc24",
                "name": "Verduras",
                "icon_url": "https://img.icons8.com/?size=100&id=11307&format=png&color=000000"
            }
        ]
    }
    """.trimIndent()


                val parsedWords = parseLibraryJson(sampleWordsJson)


                LibraryScreen(
                    userStats = userStats,
                    words = parsedWords,
                    onWordClick = { wordId -> navController.navigate("wordDetail/$wordId") }
                )
            }
            */

            // -------------------------------------------------------------
            // PROFILE – Pantalla de perfil del usuario
            // -------------------------------------------------------------
            composable(Screen.Profile.route) {
                ProfileScreen(
                    authViewModel = authViewModel,
                    userStatsViewModel = userStatsViewModel,
                    onLogoutSuccess = {
                        userStatsViewModel.clearStats()
                        onLogout()
                    },
                    onNavigateToAdmin = {
                        navController.navigate(Screen.Admin.route)
                    }
                )
            }

            // -------------------------------------------------------------
            // ADMIN – Panel de administración (solo admin/superadmin)
            // -------------------------------------------------------------
            composable(Screen.Admin.route) {
                val userStats by userStatsViewModel.userStats.collectAsState()
                val userRole by authViewModel.userRole.collectAsState()

                AdminScreen(
                    userStats = userStats,
                    userRole = userRole,
                    onBack = {
                        navController.navigateUp()
                    }
                )
            }

            // -------------------------------------------------------------
            // WORD DETAIL – Detalle de palabra/seña
            // -------------------------------------------------------------
            composable("wordDetail/{wordId}") { backStackEntry ->
                val wordId = backStackEntry.arguments?.getString("wordId") ?: return@composable
                com.req.software.amoxcalli_app.ui.screens.library.WordDetailScreen(
                    wordId = wordId,
                    onClose = {
                        navController.popBackStack()
                    }
                )
            }

            // REMOVED: Exercises/Minijuegos section - app simplified
            // Exercises route removed as per app simplification
        }
    }
}
