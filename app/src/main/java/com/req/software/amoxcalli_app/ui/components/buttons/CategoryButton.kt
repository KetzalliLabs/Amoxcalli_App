package com.req.software.amoxcalli_app.ui.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp // <-- IMPORTANTE: Importa la función lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.req.software.amoxcalli_app.ui.theme.MainColor
import com.req.software.amoxcalli_app.ui.theme.Special3Color // Importa el color verde
import com.req.software.amoxcalli_app.ui.theme.ThirdColor      // Importa el color azul oscuro

@Composable
fun CategoryButton(
    categoryName: String,
    progress: Float, // Nuevo parámetro: un valor de 0.0f a 1.0f
    onClick: () -> Unit
) {
    // 1. Calcula el color interpolado basado en el progreso
    val backgroundColor = lerp(
        start = ThirdColor,
        stop = Special3Color,
        fraction = progress
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp,
        color = backgroundColor, // 2. Usa el color calculado como fondo
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp) // Aumentamos la altura para dar espacio al nuevo texto
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Texto del nombre de la categoría
            Text(
                text = categoryName,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MainColor
            )

            Spacer(modifier = Modifier.height(8.dp)) // Espacio entre los textos

            // 3. Texto del porcentaje de progreso
            Text(
                text = "${(progress * 100).toInt()}% completado",
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = MainColor.copy(alpha = 0.8f) // Un poco más tenue
            )
        }
    }
}

// --- PREVIEWS PARA VERIFICAR EL FUNCIONAMIENTO ---

@Preview(showBackground = true, name = "Progreso 0%")
@Composable
fun CategoryButtonPreview0() {
    Box(modifier = Modifier.padding(16.dp)) {
        CategoryButton(categoryName = "Animales", progress = 0.0f, onClick = {})
    }
}

@Preview(showBackground = true, name = "Progreso 50%")
@Composable
fun CategoryButtonPreview50() {
    Box(modifier = Modifier.padding(16.dp)) {
        CategoryButton(categoryName = "Colores", progress = 0.5f, onClick = {})
    }
}

@Preview(showBackground = true, name = "Progreso 100%")
@Composable
fun CategoryButtonPreview100() {
    Box(modifier = Modifier.padding(16.dp)) {
        CategoryButton(categoryName = "Partes del Cuerpo", progress = 1.0f, onClick = {})
    }
}
