package com.req.software.amoxcalli_app.ui.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.req.software.amoxcalli_app.ui.components.buttons.LibraryWordButton
import com.req.software.amoxcalli_app.ui.components.buttons.PrimaryButton
import com.req.software.amoxcalli_app.ui.theme.ThirdColor
import com.req.software.amoxcalli_app.viewmodel.CategoryViewModel
import com.req.software.amoxcalli_app.viewmodel.FavoritesViewModel

@Composable
fun CategoryDetailScreen(
    categoryId: String,
    userStats: UserStatsResponse?,
    authToken: String?,
    onWordClick: (String) -> Unit,
    onQuizClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    categoryViewModel: CategoryViewModel = viewModel(),
    favoritesViewModel: FavoritesViewModel = viewModel()
) {
    val signs by categoryViewModel.currentCategorySigns.collectAsState()
    val isLoading by categoryViewModel.isLoading.collectAsState()
    val error by categoryViewModel.error.collectAsState()
    val favoriteIds by favoritesViewModel.favoriteIds.collectAsState()

    // Get category name from ViewModel
    val categoryName = categoryViewModel.getCategoryName(categoryId)

    // Load signs for this category
    LaunchedEffect(categoryId) {
        categoryViewModel.loadSignsForCategory(categoryId)
    }

    // Favorites load automatically from local storage in ViewModel init

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Top header (same style as other screens)
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

            // Back button and title row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = ThirdColor
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = categoryName,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = ThirdColor
                    )
                }

                // Spacer to balance the back button
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Señas de esta categoría",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quiz button
            PrimaryButton(
                text = "🎯 Quiz de $categoryName",
                onClick = onQuizClick,
                backgroundColor = Color(0xFFFFA726), // Naranja atractivo
                enablePulse = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
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
            } else if (signs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "🔍",
                            fontSize = 48.sp
                        )
                        Text(
                            text = "No hay señas en esta categoría",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                // Signs count
                Text(
                    text = "${signs.size} señas disponibles",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )


                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(signs) { sign ->
                        LibraryWordButton(
                            text = sign.name,
                            isFavorite = favoriteIds.contains(sign.id),
                            onClick = {
                                onWordClick(sign.id)
                                // Record sign view
                                authToken?.let { token ->
                                    categoryViewModel.recordSignView(sign.id, token)
                                }
                            },
                            onFavoriteClick = {
                                favoritesViewModel.toggleFavorite(
                                    signId = sign.id,
                                    name = sign.name,
                                    description = sign.description,
                                    imageUrl = sign.imageUrl,
                                    videoUrl = sign.videoUrl
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
