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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.req.software.amoxcalli_app.ui.components.buttons.LibraryTabButton
import com.req.software.amoxcalli_app.ui.components.buttons.PrimaryButton

// ----- DATA MODELS -----
data class Category(val id: String, val name: String)

@Composable
fun LibraryCategoriesScreen(
    categories: List<Category>,
    onCategoryClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { category ->
                PrimaryButton(
                    text = category.name,
                    enablePulse = false,
                    backgroundColor = Color(0xFF2196F3),
                    onClick = { onCategoryClick(category.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}