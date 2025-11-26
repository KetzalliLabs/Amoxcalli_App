package com.req.software.amoxcalli_app.ui.components.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.req.software.amoxcalli_app.R
import com.req.software.amoxcalli_app.ui.navigation.Screen
import com.req.software.amoxcalli_app.ui.theme.Special2Color
import com.req.software.amoxcalli_app.ui.theme.Special3Color

data class BottomNavItem(
    val screen: Screen,
    val iconRes: Int,
    val label: String
)

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem(
            screen = Screen.Home,
            iconRes = R.drawable.ic_home,
            label = "Inicio"
        ),
        BottomNavItem(
            screen = Screen.Topics,
            iconRes = R.drawable.ic_search,
            label = "Biblioteca"
        ),
        BottomNavItem(
            screen = Screen.Profile,
            iconRes = R.drawable.ic_profile,
            label = "Perfil"
        )
    )

    NavigationBar(
        modifier = modifier
        .height(75.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = Special2Color,       // Using theme light gray for icons
        tonalElevation = 0.dp,              // No shadow
        windowInsets = WindowInsets(0.dp)

    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = currentRoute == item.screen.route,
                onClick = { onNavigate(item.screen.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Special3Color,      // Using theme dark green
                    selectedTextColor = Special3Color,      // Using theme dark green
                    unselectedIconColor = Special2Color,    // Using theme light gray
                    unselectedTextColor = Special2Color,    // Using theme light gray
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

// ============= PREVIEW =============
@Preview(showBackground = true)
@Composable
private fun BottomNavBarPreview() {
    MaterialTheme {
        BottomNavBar(
            currentRoute = Screen.Home.route,
            onNavigate = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomNavBarTopicsSelectedPreview() {
    MaterialTheme {
        BottomNavBar(
            currentRoute = Screen.Topics.route,
            onNavigate = {}
        )
    }
}