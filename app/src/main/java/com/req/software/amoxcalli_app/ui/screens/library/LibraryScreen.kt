// File: app/src/main/java/com/req/software/amoxcalli_app/ui/screens/library/LibraryScreen.kt
package com.req.software.amoxcalli_app.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) } // 0 = nombre, 1 = categoría

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

            // Tabs (kept from previous)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Reuse a simple text button here to keep layout small (replace with LibraryTabButton if available)
                androidx.compose.material3.Button(
                    onClick = { selectedTab = 0 },
                    modifier = Modifier.weight(1f)
                ) { Text("Buscar por nombre") }
                Spacer(modifier = Modifier.width(8.dp))
                androidx.compose.material3.Button(
                    onClick = { selectedTab = 1 },
                    modifier = Modifier.weight(1f)
                ) { Text("Buscar por categoría") }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search bar placeholder (keep existing SearchBar if available)
            androidx.compose.material3.TextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text(if (selectedTab == 0) "Buscar nombre" else "Buscar categoría") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category-like list using PrimaryButton
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                val filtered = words.filter { it.name.contains(searchText, ignoreCase = true) }
                items(filtered) { word ->
                    PrimaryButton(
                        text = word.name,
                        onClick = { onWordClick(word.id) },
                        backgroundColor = if (word.isFavorite) Color(0xFFFFC107) else Color(0xFF4CAF50),
                        enablePulse = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    )
                }
            }
        }
    }
}
