package com.req.software.amoxcalli_app.ui.screens.exercises

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.req.software.amoxcalli_app.ui.screens.exercises.common.MediaType
import com.req.software.amoxcalli_app.ui.screens.exercises.games.ImageToTextGame
import com.req.software.amoxcalli_app.ui.screens.exercises.models.BaseExerciseState
import com.req.software.amoxcalli_app.ui.screens.exercises.models.ExerciseType

/**
 * Router central que decide qué pantalla de ejercicio mostrar
 *
 * DECISIÓN BASADA EN:
 * 1. ExerciseType (tipo de ejercicio: multiple_choice, matching, etc.)
 * 2. MediaType (tipo de media: VIDEO, IMAGE, NONE)
 *
 * Para "multiple_choice":
 * - Si tiene VIDEO → pantalla con video
 * - Si tiene IMAGE → pantalla con imagen
 * - Si no tiene media → pantalla solo texto
 */
@Composable
fun ExerciseRouter(
    state: BaseExerciseState,
    onOptionSelected: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (state.exercise.type) {
        ExerciseType.MULTIPLE_CHOICE -> {
            // Para multiple choice, decidir según el tipo de media
            when (state.exercise.mediaType) {
                MediaType.VIDEO -> {
                    // TODO: Crear VideoToTextGame cuando sea necesario
                    Text("Video multiple choice - Por implementar")
                }

                MediaType.IMAGE -> {
                    ImageToTextGame(
                        state = state,
                        onOptionSelected = onOptionSelected,
                        onConfirmClick = onConfirmClick,
                        onCloseClick = onCloseClick,
                        modifier = modifier
                    )
                }

                MediaType.NONE -> {
                    // Multiple choice sin media (solo texto)
                    Text("Text-only multiple choice - Por implementar")
                }
            }
        }

        ExerciseType.MATCHING -> {
            Text("Matching exercise - Por implementar")
        }

        ExerciseType.ORDERING -> {
            Text("Ordering exercise - Por implementar")
        }

        ExerciseType.FILL_BLANKS -> {
            Text("Fill blanks exercise - Por implementar")
        }
    }
}