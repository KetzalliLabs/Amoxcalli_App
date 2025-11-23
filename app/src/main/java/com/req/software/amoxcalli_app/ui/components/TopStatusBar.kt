package com.req.software.amoxcalli_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.req.software.amoxcalli_app.domain.model.UserProfile

/**
 * Top status bar showing user avatar, stats, and currency
 */
@Composable
fun TopStatusBar(
    userProfile: UserProfile,
    onShopClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: User Avatar with Level Badge
            UserAvatarWithLevel(
                avatarUrl = userProfile.avatarUrl,
                level = userProfile.level
            )

            // Center: Streak and XP
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Streak
                StatIndicator(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = Color(0xFFFF9500),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    value = userProfile.streakCount.toString()
                )

                // XP
                StatIndicator(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = "XP",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    value = userProfile.experiencePoints.toString()
                )
            }

            // Right: Virtual Currency
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.clickable { onShopClicked() }
            ) {
                // Coins
                CurrencyIndicator(
                    amount = userProfile.coins,
                    color = Color(0xFFFFD700)
                )

                // Premium Currency (if any)
                if (userProfile.premiumCurrency > 0) {
                    CurrencyIndicator(
                        amount = userProfile.premiumCurrency,
                        color = Color(0xFF58CC02),
                        isPremium = true
                    )
                }
            }
        }
    }
}

@Composable
private fun UserAvatarWithLevel(
    avatarUrl: String?,
    level: Int
) {
    Box(
        contentAlignment = Alignment.BottomEnd
    ) {
        // Avatar
        AsyncImage(
            model = avatarUrl ?: "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/KetzalliLabsLogo.jpg",
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentScale = ContentScale.Crop
        )

        // Level Badge
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = level.toString(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun StatIndicator(
    icon: @Composable () -> Unit,
    value: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun CurrencyIndicator(
    amount: Int,
    color: Color,
    isPremium: Boolean = false
) {
    Surface(
        shape = CircleShape,
        color = color.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isPremium) Icons.Default.Diamond else Icons.Default.Stars,
                contentDescription = if (isPremium) "Premium Currency" else "Coins",
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = amount.toString(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}
