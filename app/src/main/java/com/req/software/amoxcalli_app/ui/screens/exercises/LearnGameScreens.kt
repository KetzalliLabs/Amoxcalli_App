package com.req.software.amoxcalli_app.ui.screens.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.req.software.amoxcalli_app.ui.components.buttons.PrimaryButton
import com.req.software.amoxcalli_app.ui.screens.exercises.common.GameTextButton
import com.req.software.amoxcalli_app.ui.screens.exercises.common.MediaDisplay
import com.req.software.amoxcalli_app.ui.theme.ThirdColor
import com.req.software.amoxcalli_app.ui.screens.exercises.common.MediaDisplay
import com.req.software.amoxcalli_app.ui.screens.exercises.common.MediaType

// -----------------------------------------------------------------------------
//  MODELOS DE UI
// -----------------------------------------------------------------------------

enum class LearnQuestionType {
    VIDEO_TO_TEXT,
    IMAGE_TO_TEXT,
    WORD_TO_SIGN
}

data class LearnOptionUi(
    val id: String,
    val text: String
)

data class LearnGameUiState(
    val levelNumber: Int = 5,
    val topicName: String = "Vehículos",
    val coins: Int? = null,  // solo se usa en algunos layouts
    val energy: Int = 20,
    val xp: Int? = null,
    val questionType: LearnQuestionType = LearnQuestionType.VIDEO_TO_TEXT,
    val promptText: String = "¿Cuál es esta palabra?",
    val targetWord: String? = null, // para "Selecciona la seña de caballo"
    val options: List<LearnOptionUi> = emptyList(),
    val selectedOptionId: String? = null
)

// -----------------------------------------------------------------------------
//  SCREEN PRINCIPAL
// -----------------------------------------------------------------------------

@Composable
fun LearnGameScreen(
    uiState: LearnGameUiState,
    // ✅ 1. AÑADIMOS NUEVOS PARÁMETROS
    mediaType: MediaType,
    videoUrl: String?,
    imageUrl: String?,
    // -------------------------
    onOptionSelected: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6EF)) // main_color
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // --- SECCIÓN DE LA PREGUNTA (PARTE SUPERIOR) ---
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Texto de la pregunta
            Text(
                text = uiState.promptText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ThirdColor,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            // ✅ 2. LLAMADA A MediaDisplay (EL LUGAR CORRECTO)
            // Reemplaza cualquier placeholder o `when` que tuvieras aquí.
            MediaDisplay(
                mediaType = mediaType,
                videoUrl = videoUrl,
                imageUrl = imageUrl
            )
        }

        // --- SECCIÓN DE RESPUESTAS (PARTE MEDIA E INFERIOR) ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Opciones de respuesta (Grid o Column)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 24.dp)
            ) {
                items(uiState.options) { option ->
                    GameTextButton( // O GameImageButton si usas imágenes
                        text = option.text,
                        selected = uiState.selectedOptionId == option.id,
                        onClick = { onOptionSelected(option.id) }
                    )
                }
            }

            // Botón de Confirmar/Siguiente
            PrimaryButton(
                text = "Confirmar", // O "Siguiente" dependiendo del estado
                onClick = onConfirmClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
/*
@Composable
fun LearnGameScreen(
    uiState: LearnGameUiState,
    onOptionSelected: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F7)),
        color = Color(0xFFF3F4F7)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            LearnHeader(
                levelNumber = uiState.levelNumber,
                topicName = uiState.topicName,
                coins = uiState.coins,
                energy = uiState.energy,
                xp = uiState.xp,
                onCloseClick = onCloseClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            QuestionTitle(
                questionType = uiState.questionType,
                promptText = uiState.promptText,
                targetWord = uiState.targetWord
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (uiState.questionType) {
                LearnQuestionType.VIDEO_TO_TEXT -> VideoContent(
                    options = uiState.options,
                    selectedOptionId = uiState.selectedOptionId,
                    onOptionSelected = onOptionSelected
                )
                LearnQuestionType.IMAGE_TO_TEXT -> ImageContent(
                    options = uiState.options,
                    selectedOptionId = uiState.selectedOptionId,
                    onOptionSelected = onOptionSelected
                )
                LearnQuestionType.WORD_TO_SIGN -> SignContent(
                    options = uiState.options,
                    selectedOptionId = uiState.selectedOptionId,
                    onOptionSelected = onOptionSelected
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ConfirmButton(
                enabled = uiState.selectedOptionId != null,
                onClick = onConfirmClick
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}*/

// -----------------------------------------------------------------------------
//  CABECERA
// -----------------------------------------------------------------------------

@Composable
private fun LearnHeader(
    levelNumber: Int,
    topicName: String,
    coins: Int?,
    energy: Int,
    xp: Int?,
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

@Composable
private fun StatPill(text: String) {
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

// -----------------------------------------------------------------------------
//  TÍTULO DE LA PREGUNTA
// -----------------------------------------------------------------------------

@Composable
private fun QuestionTitle(
    questionType: LearnQuestionType,
    promptText: String,
    targetWord: String?
) {
    val title = when (questionType) {
        LearnQuestionType.VIDEO_TO_TEXT,
        LearnQuestionType.IMAGE_TO_TEXT -> promptText
        LearnQuestionType.WORD_TO_SIGN -> "Selecciona la seña de “${targetWord ?: ""}”"
    }

    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

// -----------------------------------------------------------------------------
//  CONTENIDOS
// -----------------------------------------------------------------------------

@Composable
private fun VideoContent(
    options: List<LearnOptionUi>,
    selectedOptionId: String?,
    onOptionSelected: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Placeholder de video
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🎥")
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Reproducir",
                    tint = Color.Black
                )
                Text("🔊")
            }
        }

        // Opciones 2x2 (simple)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            options.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { option ->
                        TextOption(
                            text = option.text,
                            selected = option.id == selectedOptionId,
                            modifier = Modifier.weight(1f),
                            onClick = { onOptionSelected(option.id) }
                        )
                    }
                    if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ImageContent(
    options: List<LearnOptionUi>,
    selectedOptionId: String?,
    onOptionSelected: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Imagen grande
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🖼️", fontSize = 48.sp)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                TextOption(
                    text = option.text,
                    selected = option.id == selectedOptionId,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onOptionSelected(option.id) }
                )
            }
        }
    }
}

@Composable
private fun SignContent(
    options: List<LearnOptionUi>,
    selectedOptionId: String?,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Grid 2x2 manual simple
        options.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { option ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                width = 2.dp,
                                color = if (option.id == selectedOptionId)
                                    Color(0xFF00C853) else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .background(
                                if (option.id == selectedOptionId)
                                    Color(0xFFE8F5E9) else Color.White
                            )
                            .clickable { onOptionSelected(option.id) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "✋", fontSize = 32.sp)
                    }
                }
                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

// -----------------------------------------------------------------------------
//  COMPONENTES
// -----------------------------------------------------------------------------

@Composable
private fun TextOption(
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
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp),
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

@Composable
private fun ConfirmButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
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
            text = "Confirmar",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
