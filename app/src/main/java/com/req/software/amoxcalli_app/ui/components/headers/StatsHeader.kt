package com.req.software.amoxcalli_app.ui.components.headers

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Compact header that shows user statistics (XP, Streak, Medals)
 * Used across all screens in the app
 * Now supports local XP tracking - Redesigned for minimal space
 */
@Composable
fun StatsHeader(
    coins: Int,
    energy: Int,
    streak: Int,
    experience: Int,
    medalsCount: Int = 0,
    modifier: Modifier = Modifier,
    useLocalXP: Boolean = true,  // Use local XP tracking instead of backend value
    localXP: Int? = null  // Optional override for local XP value
) {
    // Use local XP if provided and enabled, otherwise use backend value
    val displayXP = if (useLocalXP && localXP != null) localXP else experience

    // Gradient colors for header
    val gradientColors = listOf(
        Color(0xFF0D47A1), // Dark Blue
        Color(0xFF1565C0),
        Color(0xFF1E88E5)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = gradientColors
                    )
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // XP Stat
            CompactStatItem(
                emoji = "⭐",
                value = displayXP.toString(),
                label = "XP",
                color = Color(0xFFFFF8E1)
            )

            // Streak Stat
            CompactStatItem(
                emoji = "🔥",
                value = streak.toString(),
                label = "Racha",
                color = Color(0xFFFFE5D9)
            )

            // Medals Stat
            CompactStatItem(
                emoji = "🏆",
                value = medalsCount.toString(),
                label = "Medallas",
                color = Color(0xFFFFECB3)
            )
        }
    }
}

/**
 * Compact stat item without heavy animations
 */
@Composable
private fun CompactStatItem(
    emoji: String,
    value: String,
    label: String,
    color: Color
) {
    Card(
        modifier = Modifier
            .width(90.dp)
            .height(56.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = emoji,
                fontSize = 20.sp
            )
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = label,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black.copy(alpha = 0.6f)
                )
            }
        }
    }
}

// ============================================
// PREVIEWS
// ============================================

@Preview(name = "Stats Header - Valores Altos", showBackground = true)
@Composable
fun StatsHeaderPreview() {
    StatsHeader(
        coins = 300,
        energy = 20,
        streak = 9,
        experience = 2500,
        medalsCount = 5
    )
}

@Preview(name = "Stats Header - Valores Bajos", showBackground = true)
@Composable
fun StatsHeaderLowValuesPreview() {
    StatsHeader(
        coins = 50,
        energy = 5,
        streak = 1,
        experience = 150,
        medalsCount = 1
    )
}

@Preview(name = "Stats Header - Usuario Nuevo", showBackground = true)
@Composable
fun StatsHeaderNewUserPreview() {
    StatsHeader(
        coins = 0,
        energy = 20,
        streak = 0,
        experience = 0,
        medalsCount = 0
    )
}

@Preview(name = "Stats Header - Valores Máximos", showBackground = true)
@Composable
fun StatsHeaderMaxValuesPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        StatsHeader(
            coins = 9999,
            energy = 99,
            streak = 365,
            experience = 99999,
            medalsCount = 25
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Vista con valores extremos",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}
