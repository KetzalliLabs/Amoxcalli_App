package com.req.software.amoxcalli_app.ui.components.medals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.req.software.amoxcalli_app.data.dto.MedalInfo
import com.req.software.amoxcalli_app.data.dto.UserStatsResponse
import com.req.software.amoxcalli_app.ui.theme.MainColor
import com.req.software.amoxcalli_app.ui.theme.Special3Color
import com.req.software.amoxcalli_app.ui.theme.ThirdColor
import androidx.compose.material.icons.filled.ChevronRight
import com.req.software.amoxcalli_app.viewmodel.MedalViewModel

/**
 * Display grid of all medals with earned/locked status
 */
@Composable
fun MedalsGrid(
    userStats: UserStatsResponse?,
    modifier: Modifier = Modifier,
    medalViewModel: MedalViewModel = viewModel()
) {
    val availableMedals by medalViewModel.availableMedals.collectAsState()
    val userMedals by medalViewModel.userMedals.collectAsState()
    val isLoading by medalViewModel.isLoading.collectAsState()

    val earnedMedalIds = userMedals.map { it.medalId }.toSet()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF8F6EF))
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Medallas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ThirdColor
            )
            Text(
                text = "${userMedals.size}/${availableMedals.size}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Special3Color
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Special3Color)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(availableMedals) { medal ->
                    val isEarned = earnedMedalIds.contains(medal.id)
                    val progress = if (userStats != null) {
                        medalViewModel.getMedalProgress(medal.id, userStats)
                    } else {
                        Pair(0, 1)
                    }

                    MedalCard(
                        medal = medal,
                        isEarned = isEarned,
                        currentProgress = progress.first,
                        requiredProgress = progress.second
                    )
                }
            }
        }
    }
}

/**
 * Single medal card showing earned/locked status
 */
@Composable
private fun MedalCard(
    medal: MedalInfo,
    isEarned: Boolean,
    currentProgress: Int,
    requiredProgress: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isEarned) Color.White else Color(0xFFE0E0E0)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isEarned) 4.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Medal icon with glow for earned medals
            Box(
                contentAlignment = Alignment.Center
            ) {
                if (isEarned) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(
                                color = Special3Color.copy(alpha = 0.2f),
                                shape = CircleShape
                            )
                    )
                }

                AsyncImage(
                    model = medal.iconUrl,
                    contentDescription = medal.name,
                    modifier = Modifier
                        .size(50.dp)
                        .alpha(if (isEarned) 1f else 0.4f),
                    contentScale = ContentScale.Fit
                )

                // Lock icon for locked medals
                if (!isEarned) {
                    Text(
                        text = "🔒",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 4.dp, y = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Medal name
            Text(
                text = medal.name,
                fontSize = 12.sp,
                fontWeight = if (isEarned) FontWeight.Bold else FontWeight.Normal,
                color = if (isEarned) ThirdColor else Color.Gray,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Progress indicator for locked medals
            if (!isEarned && requiredProgress > 0) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$currentProgress/$requiredProgress",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

            // Checkmark for earned medals
            if (isEarned) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "✓",
                    fontSize = 14.sp,
                    color = Special3Color,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Compact horizontal medals display for home screen
 */
@Composable
fun CompactMedalsDisplay(
    medalCount: Int,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "🏅",
                    fontSize = 32.sp
                )
                Column {
                    Text(
                        text = "Medallas",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ThirdColor
                    )
                    Text(
                        text = "$medalCount obtenidas",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.ChevronRight,
                contentDescription = "Ver medallas",
                tint = Color.Gray
            )
        }
    }
}
