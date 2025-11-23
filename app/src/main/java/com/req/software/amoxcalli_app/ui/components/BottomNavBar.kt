package com.req.software.amoxcalli_app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.req.software.amoxcalli_app.navigation.Screen

/**
 * Bottom navigation bar for main app destinations
 */
@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

private val bottomNavItems = listOf(
    BottomNavItem(
        route = Screen.MainMenu.route,
        icon = Icons.Default.Home,
        label = "Inicio"
    ),
    BottomNavItem(
        route = Screen.Quests.route,
        icon = Icons.Default.EmojiEvents,
        label = "Logros"
    ),
    BottomNavItem(
        route = Screen.Profile.route,
        icon = Icons.Default.Person,
        label = "Perfil"
    )
)
