package com.req.software.amoxcalli_app.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.req.software.amoxcalli_app.ui.components.buttons.LibraryTabButton
import com.req.software.amoxcalli_app.ui.components.buttons.PrimaryButton

// ----- DATA MODELS -----
data class Category(val id: String, val name: String)
data class Word(val id: String, val categoryId: String, val text: String, val favorite: Boolean)

@Composable
fun LibraryCategoriesScreen(
    categories: List<Category>,
    onTabSelected: (Int) -> Unit,
    onCategoryClick: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Librería",
            fontSize = 26.sp,
            color = Color(0xFF1E88E5)
        )

        Spacer(Modifier.height(16.dp))

        // TABS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LibraryTabButton(
                text = "Buscar por nombre",
                selected = selectedTab == 0
            ) {
                selectedTab = 0
                onTabSelected(0)
            }

            LibraryTabButton(
                text = "Buscar por categoría",
                selected = selectedTab == 1
            ) {
                selectedTab = 1
                onTabSelected(1)
            }
        }

        Spacer(Modifier.height(20.dp))

        // MAIN CATEGORY CONTAINER
        Surface(
            shape = RoundedCornerShape(22.dp),
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    "Elige una categoría",
                    fontSize = 18.sp,
                    color = Color(0xFF1E88E5)
                )

                Spacer(Modifier.height(16.dp))

                // CATEGORIES - Using your PrimaryButton
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { category ->

                        PrimaryButton(
                            text = category.name,
                            enablePulse = false,
                            backgroundColor = Color(0xFF2196F3), // pick per category if needed
                            onClick = { onCategoryClick(category.id) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
