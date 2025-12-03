package com.req.software.amoxcalli_app.ui.components.notifications

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Floating notification for XP and Streak gains
 * Shows animated notification at the top of the screen
 */
@Composable
fun StatsNotification(
    notificationState: StatsNotificationState?,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    var currentNotification by remember { mutableStateOf<StatsNotificationState?>(null) }

    // Trigger animation when notification changes
    LaunchedEffect(notificationState) {
        if (notificationState != null) {
            currentNotification = notificationState
            visible = true
            delay(2500) // Show for 2.5 seconds
            visible = false
            delay(300) // Wait for exit animation
            currentNotification = null
        }
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = visible && currentNotification != null,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(300)
            ) + fadeOut()
        ) {
            currentNotification?.let { notification ->
                NotificationCard(notification = notification)
            }
        }
    }
}

@Composable
private fun NotificationCard(notification: StatsNotificationState) {
    // Bounce animation for emoji
    val infiniteTransition = rememberInfiniteTransition(label = "emoji_bounce")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .padding(16.dp)
            .widthIn(min = 200.dp, max = 300.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = when (notification.type) {
                            NotificationType.XP -> listOf(
                                Color(0xFFFFD700),
                                Color(0xFFFFA500)
                            )
                            NotificationType.STREAK -> listOf(
                                Color(0xFFFF6B6B),
                                Color(0xFFFF8E53)
                            )
                            NotificationType.COMBO -> listOf(
                                Color(0xFF667EEA),
                                Color(0xFF764BA2)
                            )
                        }
                    )
                )
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Emoji with animation
                Text(
                    text = notification.emoji,
                    fontSize = 32.sp,
                    modifier = Modifier.scale(scale)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Message
                Column {
                    Text(
                        text = notification.message,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (notification.subtitle != null) {
                        Text(
                            text = notification.subtitle,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * State for stats notifications
 */
data class StatsNotificationState(
    val type: NotificationType,
    val emoji: String,
    val message: String,
    val subtitle: String? = null
)

enum class NotificationType {
    XP,
    STREAK,
    COMBO
}

/**
 * Helper functions to create notifications
 */
object StatsNotifications {
    fun xpGained(amount: Int) = StatsNotificationState(
        type = NotificationType.XP,
        emoji = "⭐",
        message = "+$amount XP",
        subtitle = "¡Buen trabajo!"
    )

    fun streakIncreased(days: Int) = StatsNotificationState(
        type = NotificationType.STREAK,
        emoji = "🔥",
        message = "¡Racha de $days días!",
        subtitle = "Sigue así"
    )

    fun comboMultiplier(combo: Int, xpBonus: Int) = StatsNotificationState(
        type = NotificationType.COMBO,
        emoji = "⚡",
        message = "¡Combo x$combo!",
        subtitle = "+$xpBonus XP bonus"
    )

    fun exerciseCompleted(xp: Int) = StatsNotificationState(
        type = NotificationType.XP,
        emoji = "🎯",
        message = "¡Ejercicio completado!",
        subtitle = "+$xp XP"
    )

    fun dailyBonus(xp: Int) = StatsNotificationState(
        type = NotificationType.STREAK,
        emoji = "🎁",
        message = "¡Bonus diario!",
        subtitle = "+$xp XP"
    )
}
