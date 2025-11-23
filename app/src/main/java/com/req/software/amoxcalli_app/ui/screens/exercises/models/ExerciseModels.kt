package com.req.software.amoxcalli_app.ui.screens.exercises.models

import com.req.software.amoxcalli_app.ui.screens.exercises.common.MediaType

/**
 * Tipos de ejercicios según tu API
 */
enum class ExerciseType {
    MULTIPLE_CHOICE,    // "multiple_choice" del JSON
    MATCHING,           // Para futuros tipos
    ORDERING,           // Para el juego de ordenar videos
    FILL_BLANKS
}

/**
 * Respuesta completa de la API
 */
data class ExerciseResponse(
    val success: Boolean,
    val count: Int,
    val data: ExerciseData
)

/**
 * Datos del ejercicio desde la API
 */
data class ExerciseData(
    val id: String,
    val category_id: String,
    val type: String,                    // "multiple_choice"
    val question: String,                // "¿Cuál es el signo para la letra \"A\"?"
    val correct_sign_id: String,
    val category_name: String,           // "Abecedario"
    val correct_sign: SignOption,
    val options: List<SignOption>
)

/**
 * Opción de seña/signo
 */
data class SignOption(
    val id: String,
    val name: String,
    val description: String?,
    val image_url: String?,
    val video_url: String?,
    val is_correct: Boolean = false
)

/**
 * Modelo UI para el ejercicio (lo que usa Compose)
 */
data class Exercise(
    val id: String,
    val type: ExerciseType,
    val categoryName: String,
    val question: String,
    val correctAnswerId: String,
    val mediaType: MediaType,
    val questionMedia: QuestionMedia?,
    val options: List<Option>
)

data class QuestionMedia(
    val imageUrl: String?,
    val videoUrl: String?
)

data class Option(
    val id: String,
    val text: String,
    val imageUrl: String?,
    val videoUrl: String?,
    val isCorrect: Boolean = false
)

/**
 * Estado UI base para cualquier ejercicio
 */
data class BaseExerciseState(
    val exercise: Exercise,
    val selectedOptionId: String? = null,
    val isAnswerCorrect: Boolean? = null,
    val showFeedback: Boolean = false
)