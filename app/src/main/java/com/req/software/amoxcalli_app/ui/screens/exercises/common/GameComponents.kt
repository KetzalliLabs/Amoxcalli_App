package com.req.software.amoxcalli_app.ui.screens.exercises.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// -----------------------------------------------------------------------------
//  COMPONENTES REUTILIZABLES
// -----------------------------------------------------------------------------

/**
 * Header estándar para todos los ejercicios
 * Muestra nivel, tema, estadísticas y botón cerrar
 * Actualizado para coincidir con el diseño de la app
 */
@Composable
fun GameHeader(
    levelNumber: Int,
    topicName: String,
    coins: Int? = null,
    energy: Int,
    xp: Int? = null,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Fila de stats (solo si hay algo que mostrar)
        if (coins != null || xp != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                coins?.let { StatPill("💰 $it") }
                StatPill("⚡ $energy")
                xp?.let { StatPill("⭐ $it") }
            }
        }

        // Barra del tema con el color del tema de la app
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF0D1A3A), // third_color - Dark navy blue
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nivel $levelNumber · $topicName",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onCloseClick) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Píldora de estadística (monedas, energía, XP)
 * Actualizada con el diseño de la app
 */
@Composable
fun StatPill(text: String) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(50),
        shadowElevation = 3.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF0D1A3A)
        )
    }
}

/**
 * Botón de opción con texto
 * Usado para respuestas de selección múltiple
 */
@Composable
fun GameTextButton(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 3.dp,
                color = if (selected) Color(0xFF004225) else Color(0xFFC9CCD1), // special3_color : special2_color
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        color = if (selected) Color(0xFFE8F5E9) else Color.White,
        shadowElevation = if (selected) 6.dp else 2.dp
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Botón de confirmación
 * Habilitado/deshabilitado según haya selección
 * Actualizado con el estilo de la app
 */
@Composable
fun ConfirmButton(
    enabled: Boolean,
    onClick: () -> Unit,
    text: String = "Confirmar",
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF004225), // App's dark green
            disabledContainerColor = Color(0xFFE0E0E0),
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp,
            disabledElevation = 0.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

/**
 * Layout de grid 2x2 genérico
 * Útil para mostrar opciones en cuadrícula
 */
@Composable
fun GridLayout(
    items: List<@Composable () -> Unit>,
    columns: Int = 2,
    spacing: Int = 12,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items.chunked(columns).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.dp)
            ) {
                row.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        item()
                    }
                }
                // Relleno para filas incompletas
                repeat(columns - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}


/**
 * Grid dinámico que detecta automáticamente si las opciones tienen media
 */
@Composable
fun OptionsGrid(
    options: List<com.req.software.amoxcalli_app.ui.screens.exercises.models.Option>,
    selectedOptionId: String?,
    onOptionSelected: (String) -> Unit,
    columns: Int = 2,
    modifier: Modifier = Modifier
) {
    GridLayout(
        items = options.map { option ->
            {
                when {
                    // Si la opción tiene video
                    option.videoUrl != null -> GameVideoButton(
                        videoUrl = option.videoUrl,
                        selected = option.id == selectedOptionId,
                        onClick = { onOptionSelected(option.id) }
                    )
                    // Si la opción tiene imagen
                    option.imageUrl != null -> GameImageButton(
                        imageUrl = option.imageUrl,
                        selected = option.id == selectedOptionId,
                        onClick = { onOptionSelected(option.id) }
                    )
                    // Si la opción solo tiene texto
                    else -> GameTextButton(
                        text = option.text,
                        selected = option.id == selectedOptionId,
                        onClick = { onOptionSelected(option.id) }
                    )
                }
            }
        },
        columns = columns,
        modifier = modifier
    )
}

