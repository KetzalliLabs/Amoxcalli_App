package com.req.software.amoxcalli_app.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.text.style.TextAlign
import com.req.software.amoxcalli_app.ui.components.buttons.LibraryTabButton
import com.req.software.amoxcalli_app.ui.components.buttons.WordButton
import com.req.software.amoxcalli_app.ui.components.searchbars.SearchBar


// ----------------------------------------------------------------------
// MODELOS
// ----------------------------------------------------------------------
data class LibraryWordUi(
    val id: String,
    val name: String,
    val isFavorite: Boolean = false
)

// ----------------------------------------------------------------------
// PANTALLA PRINCIPAL
// ----------------------------------------------------------------------
@Composable
fun LibraryScreen(
    words: List<LibraryWordUi>,
    onWordClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) } // 0 = nombre, 1 = categoría

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Librería",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5)
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // -----------------------------------------------------------
        // TABS
        // -----------------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LibraryTabButton(
                text = "Buscar por nombre",
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 }
            )
            LibraryTabButton(
                text = "Buscar por categoría",
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -----------------------------------------------------------
        // BARRA DE BÚSQUEDA
        // -----------------------------------------------------------
        SearchBar(
            placeholder = if (selectedTab == 0) "Buscar nombre" else "Buscar categoría",
            value = searchText,
            onValueChange = { searchText = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // -----------------------------------------------------------
        // GRID DE PALABRAS (3 COLUMNAS)
        // -----------------------------------------------------------
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(words.filter {
                it.name.contains(searchText, ignoreCase = true)
            }) { word ->
                WordButton(
                    text = word.name,
                    isFavorite = word.isFavorite,
                    onClick = { onWordClick(word.id) }
                )
            }
        }
    }
}
