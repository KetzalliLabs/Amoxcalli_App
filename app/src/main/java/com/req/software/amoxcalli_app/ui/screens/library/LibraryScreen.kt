package com.req.software.amoxcalli_app.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.req.software.amoxcalli_app.data.dto.UserStatsResponse
import com.req.software.amoxcalli_app.ui.components.headers.StatsHeader
import com.req.software.amoxcalli_app.ui.components.buttons.LibraryWordButton
import com.req.software.amoxcalli_app.ui.theme.ThirdColor
import com.req.software.amoxcalli_app.viewmodel.LibraryViewModel
import com.req.software.amoxcalli_app.viewmodel.FavoritesViewModel
import com.req.software.amoxcalli_app.ui.components.searchbars.SearchBar as CustomSearchBar

data class LibraryWordUi(
    val id: String,
    val name: String,
    val videoUrl: String?,
    val imageUrl: String?,
    val isFavorite: Boolean = false
)

@Composable
fun LibraryScreen(
    onWordClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    libraryViewModel: LibraryViewModel = viewModel(),
    favoritesViewModel: FavoritesViewModel = viewModel(),
    viewedSignsViewModel: com.req.software.amoxcalli_app.viewmodel.ViewedSignsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var searchText by remember { mutableStateOf("") }
    val signs by libraryViewModel.signs.collectAsState()
    val isLoading by libraryViewModel.isLoading.collectAsState()
    val error by libraryViewModel.error.collectAsState()
    val favoriteIds by favoritesViewModel.favoriteIds.collectAsState()
    val viewedSignIds by viewedSignsViewModel.viewedSignIds.collectAsState()


    // Favorites load automatically from local storage in ViewModel init

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
        // Centralized top bar
        topBar()

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F6EF)) // main_color background
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Biblioteca de Señas",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = ThirdColor,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Explora y aprende nuevas señas",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search bar
            CustomSearchBar(
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
            } else if (filteredWords.isEmpty()) {
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
                            text = "No se encontraron señas",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                // Words count
                Text(
                    text = "${filteredWords.size} señas disponibles",
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
                    items(filteredWords) { word ->
                        // Check if this sign has been viewed - use local state
                        val isViewed = viewedSignIds.contains(word.id)

                        LibraryWordButton(
                            text = word.name,
                            isFavorite = favoriteIds.contains(word.id),
                            isViewed = isViewed,
                            onClick = {
                                // Just open the detail screen
                                onWordClick(word.id)
                            },
                            onFavoriteClick = {
                                favoritesViewModel.toggleFavorite(
                                    signId = word.id,
                                    name = word.name,
                                    imageUrl = word.imageUrl,
                                    videoUrl = word.videoUrl
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}