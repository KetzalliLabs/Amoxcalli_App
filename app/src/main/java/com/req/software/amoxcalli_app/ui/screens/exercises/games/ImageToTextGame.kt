package com.req.software.amoxcalli_app.ui.screens.exercises.games

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.req.software.amoxcalli_app.ui.screens.exercises.common.*
import com.req.software.amoxcalli_app.ui.screens.exercises.models.BaseExerciseState

/**
 * Pantalla de ejercicio: Imagen → Texto
 * Adaptada para tu JSON de la API
 */
@Composable
fun ImageToTextGame(
    state: BaseExerciseState,
    onOptionSelected: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF3F4F7)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            GameHeader(
                levelNumber = 1, // TODO: Obtener del perfil del usuario
                topicName = state.exercise.categoryName,
                energy = 20,
                coins = 100,
                onCloseClick = onCloseClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = state.exercise.question,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar imagen de la pregunta
            state.exercise.questionMedia?.let { media ->
                ImageDisplay(
                    imageUrl = media.imageUrl
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Grid de opciones (detecta automáticamente si son texto/imagen/video)
            OptionsGrid(
                options = state.exercise.options,
                selectedOptionId = state.selectedOptionId,
                onOptionSelected = onOptionSelected,
                columns = 2
            )

            Spacer(modifier = Modifier.height(24.dp))

            ConfirmButton(
                enabled = state.selectedOptionId != null,
                onClick = onConfirmClick
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}