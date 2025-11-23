package com.req.software.amoxcalli_app.data

import com.req.software.amoxcalli_app.ui.screens.exercises.common.MediaType
import com.req.software.amoxcalli_app.ui.screens.exercises.models.*

/**
 * Convierte la respuesta del API a modelo UI
 */
fun ExerciseResponse.toExercise(): Exercise {
    return data.toExercise()
}

/**
 * Convierte ExerciseData a Exercise
 */
fun ExerciseData.toExercise(): Exercise {
    // Determinar el tipo de media según el correct_sign
    val mediaType = when {
        correct_sign.video_url != null -> MediaType.VIDEO
        correct_sign.image_url != null -> MediaType.IMAGE
        else -> MediaType.NONE
    }

    // Determinar el tipo de ejercicio
    val exerciseType = when (type.lowercase()) {
        "multiple_choice" -> ExerciseType.MULTIPLE_CHOICE
        "matching" -> ExerciseType.MATCHING
        "ordering" -> ExerciseType.ORDERING
        else -> ExerciseType.MULTIPLE_CHOICE
    }

    return Exercise(
        id = id,
        type = exerciseType,
        categoryName = category_name,
        question = question,
        correctAnswerId = correct_sign_id,
        mediaType = mediaType,
        questionMedia = QuestionMedia(
            imageUrl = correct_sign.image_url,
            videoUrl = correct_sign.video_url
        ),
        options = options.map { it.toOption() }
    )
}

/**
 * Convierte SignOption a Option
 */
private fun SignOption.toOption(): Option {
    return Option(
        id = id,
        text = name,  // "A", "T", "Q", "F"
        imageUrl = image_url,
        videoUrl = video_url,
        isCorrect = is_correct
    )
}