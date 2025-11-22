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
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                coins?.let { StatPill("💰 $it") }
                StatPill("🔋 $energy")
                xp?.let { StatPill("XP $it") }
            }
        }

        // Barra del tema
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF1E88E5),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 3.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$levelNumber. $topicName",
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
 */
@Composable
fun StatPill(text: String) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(50),
        shadowElevation = 2.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            fontSize = 12.sp
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
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = 2.dp,
                color = if (selected) Color(0xFF00C853) else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() },
        color = if (selected) Color(0xFFE8F5E9) else Color.White,
        shadowElevation = if (selected) 4.dp else 1.dp
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
 * Botón de confirmación verde
 * Habilitado/deshabilitado según haya selección
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
            .height(52.dp),
        shape = RoundedCornerShape(26.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF00C853),
            disabledContainerColor = Color(0xFFE0E0E0),
            contentColor = Color.White
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
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

