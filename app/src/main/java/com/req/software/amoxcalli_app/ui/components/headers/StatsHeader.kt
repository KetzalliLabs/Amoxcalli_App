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
 * Animated header that shows user statistics (XP, Cacao, Streak, Medals)
 * Used across all screens in the app
 */
@Composable
fun StatsHeader(
    coins: Int,
    energy: Int,
    streak: Int,
    experience: Int,
    medalsCount: Int = 0,
    modifier: Modifier = Modifier
) {
    // Gradient colors for header
    val gradientColors = listOf(
        Color(0xFF6A1B9A),
        Color(0xFF8E24AA),
        Color(0xFFAB47BC),
        Color(0xFFCE93D8)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = gradientColors
                    )
                )
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Stats Row with animations
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // XP Stat
                AnimatedStatItem(
                    emoji = "⭐",
                    value = experience.toString(),
                    label = "XP",
                    color = Color(0xFFFFF8E1)
                )

                // Coins Stat (Cacao)
                /*AnimatedStatItem(
                    emoji = "🪙",
                    value = coins.toString(),
                    label = "Cacao",
                    color = Color(0xFFFFF3E0)
                )*/

                // Streak Stat
                AnimatedStatItem(
                    emoji = "🔥",
                    value = streak.toString(),
                    label = "Racha",
                    color = Color(0xFFFFE5D9)
                )

                // Medals Stat
                AnimatedStatItem(
                    emoji = "🏆",
                    value = medalsCount.toString(),
                    label = "Medallas",
                    color = Color(0xFFFFECB3)
                )
            }
        }
    }
}

/**
 * Animated stat item with bounce effect
 */
@Composable
private fun AnimatedStatItem(
    emoji: String,
    value: String,
    label: String,
    color: Color
) {
    // Bounce animation
    val infiniteTransition = rememberInfiniteTransition(label = "stat_$label")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale_$label"
    )

    Card(
        modifier = Modifier
            .scale(scale)
            .width(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = emoji,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black.copy(alpha = 0.7f)
            )
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
