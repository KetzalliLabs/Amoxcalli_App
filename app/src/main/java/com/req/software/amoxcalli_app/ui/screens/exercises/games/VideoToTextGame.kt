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
 * Pantalla de ejercicio: Video → Texto
 * El usuario ve un video y selecciona la palabra correcta
 */
//@Composable
//fun VideoToTextGame(
//    state: BaseExerciseState,
//    onOptionSelected: (String) -> Unit,
//    onConfirmClick: () -> Unit,
//    onCloseClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Surface(
//        modifier = modifier.fillMaxSize(),
//        color = Color(0xFFF3F4F7)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 16.dp, vertical = 8.dp)
//                .verticalScroll(rememberScrollState())
//        ) {
//            // Header reutilizable
//            GameHeader(
//                levelNumber = state.exercise.level,
//                topicName = state.exercise.topic,
//                energy = 20, // TODO: Obtener del estado global
//                coins = 100,
//                onCloseClick = onCloseClick
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Título de la pregunta
//            Text(
//                text = state.exercise.question.text,
//                style = MaterialTheme.typography.titleLarge,
//                fontWeight = FontWeight.SemiBold,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Video (componente reutilizable)
//            MediaDisplay(
//                mediaType = state.exercise.mediaType,
//                mediaUrl = state.exercise.question.mediaUrl
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Grid de opciones (componente reutilizable)
//            GridLayout(
//                items = state.exercise.options.map { option ->
//                    {
//                        GameTextButton(
//                            text = option.text,
//                            selected = option.id == state.selectedOptionId,
//                            onClick = { onOptionSelected(option.id) }
//                        )
//                    }
//                },
//                columns = 2
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // Botón de confirmar (componente reutilizable)
//            ConfirmButton(
//                enabled = state.selectedOptionId != null,
//                onClick = onConfirmClick
//            )
//
//            Spacer(modifier = Modifier.height(32.dp))
//        }
//    }
//}