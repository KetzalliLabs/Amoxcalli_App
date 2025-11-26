package com.req.software.amoxcalli_app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.req.software.amoxcalli_app.domain.model.UserProfile

/**
 * Top status bar showing user avatar, stats, and currency with beautiful animations
 */
@Composable
fun TopStatusBar(
    userProfile: UserProfile,
    onShopClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Gradient background colors
    val gradientColors = listOf(
        Color(0xFF6A1B9A),
        Color(0xFF8E24AA),
        Color(0xFFAB47BC)
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = gradientColors
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: User Avatar with Level Badge
                AnimatedUserAvatar(
                    avatarUrl = userProfile.avatarUrl,
                    level = userProfile.level
                )

                // Center: Streak and XP with animations
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Animated Streak
                    AnimatedStatIndicator(
                        icon = Icons.Default.LocalFireDepartment,
                        iconTint = Color(0xFFFF6B35),
                        backgroundColor = Color(0xFFFFE5D9),
                        value = userProfile.streakCount.toString(),
                        label = "🔥"
                    )

                    // Animated XP
                    AnimatedStatIndicator(
                        icon = Icons.Default.Stars,
                        iconTint = Color(0xFFFFA000),
                        backgroundColor = Color(0xFFFFF8E1),
                        value = userProfile.experiencePoints.toString(),
                        label = "⭐"
                    )
                }

                // Right: Virtual Currency
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.clickable { onShopClicked() }
                ) {
                    // Coins with animation
                    AnimatedCurrencyIndicator(
                        amount = userProfile.coins,
                        color = Color(0xFFFFD700),
                        icon = "🪙"
                    )

                    // Premium Currency (if any)
                    if (userProfile.premiumCurrency > 0) {
                        AnimatedCurrencyIndicator(
                            amount = userProfile.premiumCurrency,
                            color = Color(0xFF4CAF50),
                            icon = "💎",
                            isPremium = true
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedUserAvatar(
    avatarUrl: String?,
    level: Int
) {
    // Pulsing animation for level badge
    val infiniteTransition = rememberInfiniteTransition(label = "avatar")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        contentAlignment = Alignment.BottomEnd
    ) {
        // Avatar with shadow
        AsyncImage(
            model = avatarUrl ?: "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/KetzalliLabsLogo.jpg",
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(52.dp)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .background(Color.White),
            contentScale = ContentScale.Crop
        )

        // Animated Level Badge with gradient
        Surface(
            shape = CircleShape,
            color = Color.Transparent,
            modifier = Modifier
                .size(22.dp)
                .scale(scale)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFFD700),
                                Color(0xFFFFA000)
                            )
                        )
                    )
            ) {
                Text(
                    text = level.toString(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun AnimatedStatIndicator(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    backgroundColor: Color,
    value: String,
    label: String
) {
    // Rotation animation for icon
    val infiniteTransition = rememberInfiniteTransition(label = "stat")
    val rotation by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        modifier = Modifier.shadow(4.dp, RoundedCornerShape(20.dp))
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Animated emoji
            Text(
                text = label,
                fontSize = 18.sp,
                modifier = Modifier.rotate(rotation)
            )
            // Value
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
        }
    }
}

@Composable
private fun AnimatedCurrencyIndicator(
    amount: Int,
    color: Color,
    icon: String,
    isPremium: Boolean = false
) {
    // Bounce animation for currency
    val infiniteTransition = rememberInfiniteTransition(label = "currency")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.2f),
        modifier = Modifier.shadow(4.dp, RoundedCornerShape(20.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Animated emoji icon
            Text(
                text = icon,
                fontSize = 18.sp,
                modifier = Modifier.scale(scale)
            )
            // Amount
            Text(
                text = amount.toString(),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = color.copy(alpha = 0.9f)
            )
        }
    }
}
