// File: app/src/main/java/com/req/software/amoxcalli_app/ui/screens/library/LibraryScreen.kt
package com.req.software.amoxcalli_app.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.req.software.amoxcalli_app.data.dto.UserStatsResponse
import com.req.software.amoxcalli_app.ui.components.buttons.PrimaryButton
import com.req.software.amoxcalli_app.ui.components.headers.StatsHeader
import com.req.software.amoxcalli_app.ui.components.buttons.LibraryWordButton
import com.req.software.amoxcalli_app.ui.components.searchbars.SearchBar
import com.req.software.amoxcalli_app.ui.theme.ThirdColor
import com.req.software.amoxcalli_app.viewmodel.LibraryViewModel

data class LibraryWordUi(
    val id: String,
    val name: String,
    val videoUrl: String?,
    val imageUrl: String?,
    val isFavorite: Boolean = false
)

@Composable
fun LibraryScreen(
    userStats: UserStatsResponse?,
    authToken: String?,
    onWordClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    libraryViewModel: LibraryViewModel = viewModel()
) {
    var searchText by remember { mutableStateOf("") }
    val signs by libraryViewModel.signs.collectAsState()
    val categories by libraryViewModel.categories.collectAsState()
    val isLoading by libraryViewModel.isLoading.collectAsState()
    val error by libraryViewModel.error.collectAsState()

    // Map signs to LibraryWordUi
    val words = signs.map {
        LibraryWordUi(
            id = it.id,
            name = it.name,
            videoUrl = it.videoUrl,
            imageUrl = it.imageUrl,
            isFavorite = false
        )
    }

    val filteredWords = if (searchText.isBlank()) {
        words
    } else {
        words.filter { word ->
            word.name.contains(searchText, ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Top header (same style as Home)
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
                experience = experience
            )
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F6EF)) // main_color background
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Librería",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Search bar placeholder
            SearchBar(
                placeholder = "Buscar seña...",
                value = searchText,
                onValueChange = { searchText = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Show loading indicator
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (error != null) {
                // Show error message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error ?: "Error desconocido",
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredWords) { word ->
                        // Use the existing LibraryWordButton component
                        LibraryWordButton(
                            text = word.name,
                            isFavorite = false,
                            onClick = {
                                onWordClick(word.id)
                                // Record sign view
                                authToken?.let { token ->
                                    libraryViewModel.recordSignView(word.id, token)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}