package com.req.software.amoxcalli_app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.req.software.amoxcalli_app.data.dto.Medal
import com.req.software.amoxcalli_app.domain.model.UserStats
import com.req.software.amoxcalli_app.ui.components.buttons.PrimaryButton
import com.req.software.amoxcalli_app.ui.components.headers.StatsHeader
import com.req.software.amoxcalli_app.ui.theme.Special3Color
import com.req.software.amoxcalli_app.ui.theme.ThirdColor
import com.req.software.amoxcalli_app.ui.components.notifications.StatsNotification
import com.req.software.amoxcalli_app.ui.components.notifications.StatsNotificationState

/**
 * Simplified Home Screen
 * Shows user stats, medals, and main action buttons (Daily Quiz, Word Practice, Library)
 * Now supports local XP tracking and animated notifications
 */
@Composable
fun HomeScreen(
    userStats: UserStats,
    medals: List<Medal> = emptyList(),
    onQuizClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onLibraryClick: () -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    notification: StatsNotificationState? = null
) {
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                topBar()
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Enhanced Stats Section with Medals
            if (medals.isNotEmpty()) {
                Text(
                    text = "Tus Medallas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                MedalsSection(medals = medals)

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Main Action Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // Daily Quiz
                ActionCard(
                    title = "Quiz Diario",
                    description = "10 preguntas aleatorias",
                    buttonText = "Comenzar Quiz",
                    onClick = onQuizClick,
                    backgroundColor = Special3Color,
                    icon = "📝"
                )

                // Word Practice
                ActionCard(
                    title = "Práctica de Palabras",
                    description = "Refuerza tu vocabulario",
                    buttonText = "Practicar",
                    onClick = onPracticeClick,
                    backgroundColor = Special3Color,
                    icon = "✍️"
                )

                // Library Shortcut
                ActionCard(
                    title = "Biblioteca LSM",
                    description = "Explora todas las señas",
                    buttonText = "Ver Biblioteca",
                    onClick = onLibraryClick,
                    backgroundColor = Color(0xFF2196F3),
                    icon = "📚"
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Notification overlay
        StatsNotification(
            notificationState = notification,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

/**
 * Medals section showing user achievements
 */
@Composable
private fun MedalsSection(
    medals: List<Medal>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
    ) {
        medals.take(4).forEach { medal ->
            MedalItem(medal = medal)
        }
    }
}

/**
 * Individual medal display
 */
@Composable
private fun MedalItem(
    medal: Medal,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (medal.iconUrl != null) {
            AsyncImage(
                model = medal.iconUrl,
                contentDescription = medal.name,
                modifier = Modifier.size(48.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = medal.name,
                modifier = Modifier.size(48.dp),
                tint = Color(0xFFFFD700) // Gold color
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = medal.name,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

/**
 * Action card with title, description, and button
 */
@Composable
private fun ActionCard(
    title: String,
    description: String,
    buttonText: String,
    onClick: () -> Unit,
    backgroundColor: Color,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 48.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = buttonText,
                onClick = onClick,
                backgroundColor = backgroundColor,
                enablePulse = true
            )
        }
    }
}

// ============================================
// PREVIEW
// ============================================

@Preview(
    name = "Simplified Home Screen",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenPreview() {
    val sampleUserStats = UserStats(
        coins = 300,
        energy = 20,
        streak = 9,
        experience = 2500
    )

    val sampleMedals = listOf(
        Medal(
            id = "1",
            medalId = "jade",
            name = "Jade",
            description = "Primera medalla",
            iconUrl = null,
            achievedAt = "2025-01-01"
        ),
        Medal(
            id = "2",
            medalId = "obsidiana",
            name = "Obsidiana",
            description = "Segunda medalla",
            iconUrl = null,
            achievedAt = "2025-01-02"
        )
    )

    HomeScreen(
        userStats = sampleUserStats,
        medals = sampleMedals,
        onQuizClick = {},
        onPracticeClick = {},
        onLibraryClick = {}
    )
}

@Preview(
    name = "Home Screen - New User",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenNewUserPreview() {
    val sampleUserStats = UserStats(
        coins = 0,
        energy = 20,
        streak = 0,
        experience = 0
    )

    HomeScreen(
        userStats = sampleUserStats,
        medals = emptyList(),
        onQuizClick = {},
        onPracticeClick = {},
        onLibraryClick = {}
    )
}
