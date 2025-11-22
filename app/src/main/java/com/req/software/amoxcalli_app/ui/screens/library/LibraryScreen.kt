// File: app/src/main/java/com/req/software/amoxcalli_app/ui/screens/library/LibraryScreen.kt
package com.req.software.amoxcalli_app.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.req.software.amoxcalli_app.domain.model.UserStats
import com.req.software.amoxcalli_app.ui.components.buttons.PrimaryButton
import com.req.software.amoxcalli_app.ui.components.headers.StatsHeader
import com.req.software.amoxcalli_app.ui.components.buttons.LibraryWordButton
import com.req.software.amoxcalli_app.ui.components.searchbars.SearchBar

data class LibraryWordUi(
    val id: String,
    val name: String,
    val isFavorite: Boolean = false
)

@Composable
fun LibraryScreen(
    userStats: UserStats,
    words: List<LibraryWordUi>,
    onWordClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchText by remember { mutableStateOf("") }
    val filteredWords = if (searchText.isBlank()) {
        words // Si no hay texto, muestra todas las palabras.
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
                .background(Color(0xFF2196F3))
                .padding(top = 12.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StatsHeader(
                coins = userStats.coins,
                energy = userStats.energy,
                streak = userStats.streak,
                experience = userStats.experience
            )
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Librería",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5),
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
                        isFavorite = false, // favorites removed;
                        onClick = { onWordClick(word.id) },
                        //modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}