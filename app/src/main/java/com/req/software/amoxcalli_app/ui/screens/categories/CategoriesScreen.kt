package com.req.software.amoxcalli_app.ui.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.req.software.amoxcalli_app.data.dto.UserStatsResponse
import com.req.software.amoxcalli_app.ui.components.headers.StatsHeader
import com.req.software.amoxcalli_app.ui.components.buttons.PrimaryButton
import com.req.software.amoxcalli_app.ui.theme.ThirdColor
import com.req.software.amoxcalli_app.ui.theme.Special3Color
import com.req.software.amoxcalli_app.viewmodel.CategoryViewModel

@Composable
fun CategoriesScreen(
    userStats: UserStatsResponse?,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    categoryViewModel: CategoryViewModel = viewModel(),
    viewedSignsViewModel: com.req.software.amoxcalli_app.viewmodel.ViewedSignsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val categories by categoryViewModel.categories.collectAsState()
    val isLoading by categoryViewModel.isLoading.collectAsState()
    val error by categoryViewModel.error.collectAsState()
    val categorySignsMap by categoryViewModel.categorySignsMap.collectAsState()
    val viewedSignIds by viewedSignsViewModel.viewedSignIds.collectAsState()

    // Preload signs for all categories to calculate progress
    LaunchedEffect(categories) {
        if (categories.isNotEmpty()) {
            categoryViewModel.preloadAllCategorySigns()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Top header (same style as Library and Home)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ThirdColor) // Using theme color - Dark navy blue
                .padding(top = 12.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Extract values from UserStatsResponse
            val coins = userStats?.stats?.find { it.name == "coins" }?.currentValue ?: 0
            val energy = userStats?.stats?.find { it.name == "energy" }?.currentValue ?: 0
            val streak = userStats?.streak?.currentDays ?: 0
            val experience = userStats?.stats?.find { it.name == "experience_points" }?.currentValue ?: 0

            StatsHeader(
                coins = coins,
                energy = energy,
                streak = streak,
                experience = experience,
                medalsCount = userStats?.medals?.size ?: 0
            )
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F6EF)) // main_color background
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Práctica de palabras",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = ThirdColor,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Explora señas por categoría",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Show loading indicator
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ThirdColor)
                }
            } else if (error != null) {
                // Show error message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "⚠️",
                            fontSize = 48.sp
                        )
                        Text(
                            text = error ?: "Error desconocido",
                            color = Color.Red,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else if (categories.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "📂",
                            fontSize = 48.sp
                        )
                        Text(
                            text = "No hay categorías disponibles",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                // Categories count
                Text(
                    text = "${categories.size} categorías disponibles",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(categories) { category ->
                        // Calculate progress percentage using local viewed state
                        val categorySignsList = categorySignsMap[category.id] ?: emptyList()
                        val categorySignsCount = categorySignsList.size

                        val viewedSignsInCategory = categorySignsList.count { sign ->
                            viewedSignIds.contains(sign.id)
                        }

                        val progressPercentage = if (categorySignsCount > 0) {
                            ((viewedSignsInCategory.toFloat() / categorySignsCount) * 100).toInt()
                        } else {
                            0
                        }

                        CategoryButtonWithProgress(
                            categoryName = category.name,
                            progress = progressPercentage,
                            onClick = { onCategoryClick(category.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Category button with progress percentage
 */
@Composable
private fun CategoryButtonWithProgress(
    categoryName: String,
    progress: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = ThirdColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Category name
            Text(
                text = categoryName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Progress bar
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = progress / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = Color(0xFF4CAF50), // Bright green for better visibility
                    trackColor = Color.White.copy(alpha = 0.4f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Progress percentage
                Text(
                    text = "$progress%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}
