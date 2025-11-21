package com.req.software.amoxcalli_app.ui.components.buttons

// Agrega este Composable en un archivo apropiado, como /ui/components/buttons/LibraryWordButton.ktpackage com.req.software.amoxcalli_app.ui.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Nuevo Composable para el botón en LibraryScreen
@Composable
fun LibraryWordButton(
    text: String,
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp), // Esquinas un poco más redondeadas
        shadowElevation = 4.dp,           // Sombra más pronunciada
        color = Color(0xFF4A90E2),          // Color de fondo primario (ejemplo: azul)
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            //.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            // Icono de favorito en la esquina superior derecha
            Text(
                text = if (isFavorite) "❤️" else "",
                fontSize = 20.sp, // Un poco más grande para que se note
                modifier = Modifier
                    .align(Alignment.TopEnd) // Alineado arriba a la derecha
                    .padding(top = 4.dp, end = 4.dp)
            )

            // Texto centrado
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold, // Letra en negrita para mejor contraste
                color = Color.White,          // Color de texto blanco
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LibraryWordButtonPreview() {
    Box(modifier = Modifier.padding(16.dp).size(120.dp)) {
        LibraryWordButton(text = "Hola", isFavorite = true, onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun LibraryWordButtonNotFavoritePreview() {
    Box(modifier = Modifier.padding(16.dp).size(120.dp)) {
        LibraryWordButton(text = "Adiós", isFavorite = false, onClick = {})
    }
}
