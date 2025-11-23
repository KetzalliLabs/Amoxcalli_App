package com.req.software.amoxcalli_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.req.software.amoxcalli_app.domain.model.CompletionState
import com.req.software.amoxcalli_app.domain.model.Lesson
import com.req.software.amoxcalli_app.domain.model.LessonType

/**
 * Individual lesson node representing a skill circle
 */
@Composable
fun LessonNode(
    lesson: Lesson,
    onLessonClicked: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isClickable = !lesson.isLocked
    val alpha = if (lesson.isLocked) 0.4f else 1f

    Column(
        modifier = modifier
            .alpha(alpha)
            .clickable(enabled = isClickable) {
                onLessonClicked(lesson.id, lesson.displayName)
            }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Lesson Circle with Icon
        Box(
            contentAlignment = Alignment.Center
        ) {
            // Background Circle
            Surface(
                shape = CircleShape,
                color = getLessonColor(lesson),
                modifier = Modifier.size(72.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = getLessonIcon(lesson),
                        contentDescription = lesson.displayName,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            // Progress Ring (if in progress)
            if (lesson.completionState == CompletionState.IN_PROGRESS && lesson.progressPercent > 0) {
                CircularProgressIndicator(
                    progress = { lesson.progressPercent / 100f },
                    modifier = Modifier.size(76.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp,
                )
            }

            // Lock Icon
            if (lesson.isLocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.BottomEnd)
                        .background(Color.Gray, CircleShape)
                        .padding(4.dp)
                )
            }

            // Checkpoint Crown
            if (lesson.isCheckpoint) {
                Icon(
                    imageVector = Icons.Default.Stars,
                    contentDescription = "Checkpoint",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.TopEnd)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lesson Name
        Text(
            text = lesson.displayName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.widthIn(max = 80.dp)
        )

        // Stars Indicator
        if (lesson.starsEarned > 0) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                repeat(lesson.maxStars) { index ->
                    Icon(
                        imageVector = if (index < lesson.starsEarned) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = null,
                        tint = if (index < lesson.starsEarned) Color(0xFFFFC107) else Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

private fun getLessonColor(lesson: Lesson): Color {
    return when {
        lesson.isLocked -> Color(0xFF9E9E9E)
        lesson.isCheckpoint -> Color(0xFFFF6B35)
        lesson.completionState == CompletionState.COMPLETED -> Color(0xFF58CC02)
        lesson.completionState == CompletionState.IN_PROGRESS -> Color(0xFF1CB0F6)
        else -> Color(0xFF3C3C3C)
    }
}

private fun getLessonIcon(lesson: Lesson): androidx.compose.ui.graphics.vector.ImageVector {
    return when (lesson.type) {
        LessonType.STORY -> Icons.Default.AutoStories
        LessonType.PRACTICE -> Icons.Default.FitnessCenter
        LessonType.CHECKPOINT -> Icons.Default.EmojiEvents
        else -> Icons.Default.School
    }
}
