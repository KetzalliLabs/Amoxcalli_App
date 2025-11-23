package com.req.software.amoxcalli_app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Special3Color,           // Dark green for primary actions
    secondary = Special2Color,         // Light gray for secondary elements
    tertiary = ThirdColor,             // Navy blue for tertiary
    background = Special1Color,        // Dark gray background
    surface = ComplementColor,         // Almost black for surfaces
    onPrimary = MainColor,            // Light cream text on primary
    onSecondary = ComplementColor,    // Dark text on secondary
    onTertiary = MainColor,           // Light cream text on tertiary
    onBackground = MainColor,         // Light cream text on background
    onSurface = MainColor             // Light cream text on surface
)

private val LightColorScheme = lightColorScheme(
    primary = Special3Color,           // Dark green for primary actions
    secondary = Special2Color,         // Light gray for secondary elements
    tertiary = ThirdColor,             // Navy blue for tertiary
    background = MainColor,            // Light cream background
    surface = MainColor,               // Light cream for surfaces
    onPrimary = MainColor,            // Light cream text on primary
    onSecondary = ComplementColor,    // Dark text on secondary
    onTertiary = MainColor,           // Light cream text on tertiary
    onBackground = ComplementColor,   // Dark text on background
    onSurface = ComplementColor       // Dark text on surface
)

@Composable
fun Amoxcalli_AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+ but we disable it to use our custom palette
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}