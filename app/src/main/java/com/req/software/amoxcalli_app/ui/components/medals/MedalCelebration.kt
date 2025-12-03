package com.req.software.amoxcalli_app.ui.components.medals

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.req.software.amoxcalli_app.data.dto.MedalClaimResponse
import com.req.software.amoxcalli_app.ui.theme.MainColor
import com.req.software.amoxcalli_app.ui.theme.Special3Color
import com.req.software.amoxcalli_app.ui.theme.ThirdColor
import kotlin.math.cos
import kotlin.math.sin

/**
 * Medal celebration dialog with animations
 * Shows when user earns a new medal
 */
@Composable
fun MedalCelebrationDialog(
    medal: MedalClaimResponse,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            // Confetti background
            ConfettiAnimation()

            // Medal card
            MedalCard(
                medal = medal,
                onDismiss = onDismiss
            )
        }
    }
}

/**
 * Animated confetti particles
 */
@Composable
private fun ConfettiAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")

    // Create multiple confetti particles
    repeat(20) { index ->
        val angle = (360f / 20f) * index
        val offsetX by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 300f * cos(Math.toRadians(angle.toDouble())).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "confettiX$index"
        )
        val offsetY by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 300f * sin(Math.toRadians(angle.toDouble())).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "confettiY$index"
        )

        val alpha by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "confettiAlpha$index"
        )

        Box(
            modifier = Modifier
                .offset(offsetX.dp, offsetY.dp)
                .size(8.dp)
                .alpha(alpha)
                .background(
                    color = when (index % 4) {
                        0 -> Special3Color
                        1 -> MainColor
                        2 -> ThirdColor
                        else -> Color(0xFFFFA726)
                    },
                    shape = if (index % 2 == 0) CircleShape else RoundedCornerShape(2.dp)
                )
        )
    }
}

/**
 * Medal card with animation
 */
@Composable
private fun MedalCard(
    medal: MedalClaimResponse,
    onDismiss: () -> Unit
) {
    // Scale animation
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "medalScale"
    )

    // Rotation animation for medal icon
    val infiniteTransition = rememberInfiniteTransition(label = "medalRotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .scale(scale),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 16.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFF9E6),
                            Color.White
                        )
                    )
                )
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Celebration emoji
            Text(
                text = "🎉",
                fontSize = 48.sp,
                modifier = Modifier.offset(y = (-8).dp)
            )

            // Title
            Text(
                text = "¡Medalla Desbloqueada!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = ThirdColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Medal icon with rotation and glow
            Box(
                contentAlignment = Alignment.Center
            ) {
                // Glow effect
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(
                            color = Special3Color.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                )

                // Medal image
                AsyncImage(
                    model = medal.iconUrl,
                    contentDescription = medal.name,
                    modifier = Modifier
                        .size(180.dp)
                        .scale(scale),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Medal name
            Text(
                text = medal.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ThirdColor,
                textAlign = TextAlign.Center
            )

            // Medal description
            Text(
                text = medal.description,
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dismiss button
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Special3Color
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "¡Genial!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * Preview for medal card
 */
@Composable
fun MedalCelebrationPreview() {
    val sampleMedal = MedalClaimResponse(
        userMedalId = "sample-id",
        medalId = "medal-id",
        name = "Tlaolli",
        description = "3 días de racha",
        iconUrl = "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/M_Tlaolli.png",
        achievedAt = "2025-12-01T00:00:00.000Z"
    )

    MedalCelebrationDialog(
        medal = sampleMedal,
        onDismiss = {}
    )
}
