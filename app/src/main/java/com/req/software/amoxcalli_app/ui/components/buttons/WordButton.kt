package com.req.software.amoxcalli_app.ui.components.buttons

// Agrega este Composable en un archivo apropiado, como /ui/components/buttons/LibraryWordButton.ktpackage com.req.software.amoxcalli_app.ui.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.req.software.amoxcalli_app.ui.theme.MainColor

// Nuevo Composable para el botón en LibraryScreen
@Composable
fun LibraryWordButton(
    text: String,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: (() -> Unit)? = null
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp,
        color = Color(0xFF0D1A3A), // third_color - Dark navy blue
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.35f)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            // Icono de favorito en la esquina superior derecha
            if (isFavorite) {
                Text(
                    text = "✅",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(2.dp)
                        .clickable(
                            onClick = onFavoriteClick,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                ) {
                    Text(
                        text = if (isFavorite) "❤️" else "🤍",
                        fontSize = 16.sp
                    )
                }
            }

            // Texto centrado
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MainColor,
                modifier = Modifier.align(Alignment.Center),
                maxLines = 2
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LibraryWordButtonPreview() {
    Box(modifier = Modifier.padding(16.dp).size(120.dp)) {
        LibraryWordButton(text = "Hola", isFavorite = true, onClick = {}, onFavoriteClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun LibraryWordButtonNotFavoritePreview() {
    Box(modifier = Modifier.padding(16.dp).size(120.dp)) {
        LibraryWordButton(text = "Adiós", isFavorite = false, onClick = {}, onFavoriteClick = {})
    }
}
