package com.req.software.amoxcalli_app.ui.screens.exercises

import kotlin.jvm.java

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.req.software.amoxcalli_app.data.toExercise
import com.req.software.amoxcalli_app.ui.screens.exercises.common.MediaType
import com.req.software.amoxcalli_app.ui.screens.exercises.models.ExerciseResponse
import com.req.software.amoxcalli_app.ui.screens.exercises.models.BaseExerciseState
import com.req.software.amoxcalli_app.ui.screens.exercises.models.Exercise
import com.req.software.amoxcalli_app.ui.screens.exercises.models.ExerciseType
import com.req.software.amoxcalli_app.ui.screens.exercises.models.Option
import com.req.software.amoxcalli_app.ui.screens.exercises.models.QuestionMedia

/**
 * Pantalla de prueba para verificar el flujo completo:
 * JSON → Mapper → Router → Pantalla
 */
@Preview
@Composable
fun ExerciseTestScreen(
    onBackClick: () -> Unit = {}
) {
    // JSON de ejemplo (el que te proporcionaron)
    val jsonExample = """
        {
            "success": true,
            "count": 1,
            "data": {
                "id": "b0c0a085-8bde-4456-b905-c250e1ab2684",
                "category_id": "fb59bac7-7119-4c93-837e-f12469bf699b",
                "type": "multiple_choice",
                "question": "¿Cuál es el signo para la letra \"A\"?",
                "correct_sign_id": "396f25c7-1c08-41a5-8fa8-5a8b3fbe4e7f",
                "category_name": "Abecedario",
                "correct_sign": {
                    "id": "396f25c7-1c08-41a5-8fa8-5a8b3fbe4e7f",
                    "name": "A",
                    "description": null,
                    "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/a.JPG",
                    "video_url": null
                },
                "options": [
                    {
                        "id": "396f25c7-1c08-41a5-8fa8-5a8b3fbe4e7f",
                        "name": "A",
                        "description": null,
                        "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/a.JPG",
                        "video_url": null,
                        "is_correct": true
                    },
                    {
                        "id": "676c70af-60bb-4758-8a82-a030ce3563ec",
                        "name": "T",
                        "description": null,
                        "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/t.JPG",
                        "video_url": null,
                        "is_correct": false
                    },
                    {
                        "id": "a77a46d5-49d7-413e-af41-5f9ad65bda29",
                        "name": "Q",
                        "description": null,
                        "image_url": null,
                        "video_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/Q_Web.m4v",
                        "is_correct": false
                    },
                    {
                        "id": "20d8a375-1188-40f1-aa3e-5b71d4c25dbb",
                        "name": "F",
                        "description": null,
                        "image_url": "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/f.JPG",
                        "video_url": null,
                        "is_correct": false
                    }
                ]
            }
        }
    """.trimIndent()

    // Estado del ejercicio
    var exerciseState by remember { mutableStateOf<BaseExerciseState?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Procesar JSON al cargar la pantalla
    LaunchedEffect(Unit) {
        try {
            // 1. Parsear JSON a modelo de API
            val apiResponse = Gson().fromJson(jsonExample, ExerciseResponse::class.java)

            // 2. Usar el MAPPER para convertir a modelo UI
            val exercise = apiResponse.toExercise()

            // 3. Crear el estado del ejercicio
            exerciseState = BaseExerciseState(
                exercise = exercise,
                selectedOptionId = null
            )
        } catch (e: Exception) {
            errorMessage = "Error al procesar ejercicio: ${e.message}"
        }
    }

    // Manejo de selección de opciones
    fun onOptionSelected(optionId: String) {
        exerciseState = exerciseState?.copy(selectedOptionId = optionId)
    }

    // Manejo de confirmación
    fun onConfirmClick() {
        val currentState = exerciseState ?: return
        val selectedOption = currentState.exercise.options.find {
            it.id == currentState.selectedOptionId
        }

        exerciseState = currentState.copy(
            isAnswerCorrect = selectedOption?.isCorrect,
            showFeedback = true
        )
    }

    // UI
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            // Mostrar error si hay
            errorMessage != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "❌ Error",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = errorMessage ?: "Error desconocido")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onBackClick) {
                        Text("Volver")
                    }
                }
            }

            // Mostrar ejercicio si está listo
            exerciseState != null -> {
                // 4. Usar el ROUTER para mostrar la pantalla correcta
                ExerciseRouter(
                    state = exerciseState!!,
                    onOptionSelected = ::onOptionSelected,
                    onConfirmClick = ::onConfirmClick,
                    onCloseClick = onBackClick
                )

                // Mostrar feedback si se respondió
                if (exerciseState!!.showFeedback) {
                    FeedbackOverlay(
                        isCorrect = exerciseState!!.isAnswerCorrect == true,
                        onDismiss = {
                            exerciseState = exerciseState!!.copy(showFeedback = false)
                        }
                    )
                }
            }

            // Mostrar loading
            else -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

/**
 * Overlay que muestra si la respuesta fue correcta o incorrecta
 */
@Composable
private fun FeedbackOverlay(
    isCorrect: Boolean,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isCorrect)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isCorrect) "✅ ¡Correcto!" else "❌ Incorrecto",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismiss) {
                    Text("Continuar")
                }
            }
        }
    }
}


// -----------------------------------------------------------------------------
//  PREVIEWS
// -----------------------------------------------------------------------------

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ExerciseRouterImagePreview() {
    val sampleExercise = Exercise(
        id = "preview-001",
        type = ExerciseType.MULTIPLE_CHOICE,
        categoryName = "Abecedario",
        question = "¿Cuál es el signo para la letra \"A\"?",
        correctAnswerId = "opt1",
        mediaType = MediaType.IMAGE,
        questionMedia = QuestionMedia(
            imageUrl = "https://example.com/a.jpg",
            videoUrl = null
        ),
        options = listOf(
            Option(id = "opt1", text = "A", imageUrl = null, videoUrl = null, isCorrect = true),
            Option(id = "opt2", text = "B", imageUrl = null, videoUrl = null, isCorrect = false),
            Option(id = "opt3", text = "C", imageUrl = null, videoUrl = null, isCorrect = false),
            Option(id = "opt4", text = "D", imageUrl = null, videoUrl = null, isCorrect = false)
        )
    )

    val sampleState = BaseExerciseState(
        exercise = sampleExercise,
        selectedOptionId = null
    )

    ExerciseRouter(
        state = sampleState,
        onOptionSelected = {},
        onConfirmClick = {},
        onCloseClick = {}
    )
}