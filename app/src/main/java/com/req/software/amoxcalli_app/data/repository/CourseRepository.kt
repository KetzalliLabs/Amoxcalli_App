package com.req.software.amoxcalli_app.data.repository

import com.req.software.amoxcalli_app.domain.model.*
import kotlinx.coroutines.delay

/**
 * Repository for course and learning path data
 * MVP: Uses in-memory cache with mock data
 */
class CourseRepository {
    private var cachedLearningPath: LearningPath? = null

    suspend fun getLearningPath(userId: String, forceRefresh: Boolean = false): Result<LearningPath> {
        return try {
            if (!forceRefresh && cachedLearningPath != null) {
                return Result.success(cachedLearningPath!!)
            }

            delay(800)

            val learningPath = createMockLearningPath(userId)
            cachedLearningPath = learningPath
            Result.success(learningPath)
        } catch (e: Exception) {
            if (cachedLearningPath != null) {
                Result.success(cachedLearningPath!!)
            } else {
                Result.failure(e)
            }
        }
    }

    private fun createMockLearningPath(userId: String): LearningPath {
        val units = listOf(
            LearningUnit(
                id = "unit_1",
                displayName = "Fundamentos",
                description = "Aprende los conceptos básicos",
                orderIndex = 0,
                isLocked = false,
                lessons = listOf(
                    Lesson(
                        id = "lesson_1_1",
                        unitId = "unit_1",
                        displayName = "Introducción",
                        description = "Primeros pasos",
                        iconUrl = null,
                        orderIndex = 0,
                        completionState = CompletionState.COMPLETED,
                        progressPercent = 100,
                        isLocked = false,
                        starsEarned = 3,
                        maxStars = 3
                    ),
                    Lesson(
                        id = "lesson_1_2",
                        unitId = "unit_1",
                        displayName = "Vocabulario básico",
                        description = "Palabras esenciales",
                        iconUrl = null,
                        orderIndex = 1,
                        completionState = CompletionState.COMPLETED,
                        progressPercent = 100,
                        isLocked = false,
                        starsEarned = 2,
                        maxStars = 3
                    ),
                    Lesson(
                        id = "lesson_1_3",
                        unitId = "unit_1",
                        displayName = "Práctica",
                        description = "Refuerza lo aprendido",
                        iconUrl = null,
                        orderIndex = 2,
                        completionState = CompletionState.IN_PROGRESS,
                        progressPercent = 60,
                        isLocked = false,
                        starsEarned = 1,
                        maxStars = 3,
                        type = LessonType.PRACTICE
                    ),
                    Lesson(
                        id = "lesson_1_4",
                        unitId = "unit_1",
                        displayName = "Checkpoint",
                        description = "Evaluación de unidad",
                        iconUrl = null,
                        orderIndex = 3,
                        completionState = CompletionState.NOT_STARTED,
                        progressPercent = 0,
                        isLocked = false,
                        isCheckpoint = true,
                        type = LessonType.CHECKPOINT
                    )
                )
            ),
            LearningUnit(
                id = "unit_2",
                displayName = "Nivel Intermedio",
                description = "Expande tu conocimiento",
                orderIndex = 1,
                isLocked = false,
                lessons = listOf(
                    Lesson(
                        id = "lesson_2_1",
                        unitId = "unit_2",
                        displayName = "Gramática avanzada",
                        description = "Estructuras complejas",
                        iconUrl = null,
                        orderIndex = 0,
                        completionState = CompletionState.NOT_STARTED,
                        progressPercent = 0,
                        isLocked = false
                    ),
                    Lesson(
                        id = "lesson_2_2",
                        unitId = "unit_2",
                        displayName = "Conversación",
                        description = "Práctica oral",
                        iconUrl = null,
                        orderIndex = 1,
                        completionState = CompletionState.NOT_STARTED,
                        progressPercent = 0,
                        isLocked = false
                    ),
                    Lesson(
                        id = "lesson_2_3",
                        unitId = "unit_2",
                        displayName = "Historia cultural",
                        description = "Conoce la cultura",
                        iconUrl = null,
                        orderIndex = 2,
                        completionState = CompletionState.NOT_STARTED,
                        progressPercent = 0,
                        isLocked = false,
                        type = LessonType.STORY
                    )
                )
            ),
            LearningUnit(
                id = "unit_3",
                displayName = "Nivel Avanzado",
                description = "Domina el idioma",
                orderIndex = 2,
                isLocked = true,
                lessons = listOf(
                    Lesson(
                        id = "lesson_3_1",
                        unitId = "unit_3",
                        displayName = "Fluidez",
                        description = "Perfecciona tu habilidad",
                        iconUrl = null,
                        orderIndex = 0,
                        completionState = CompletionState.NOT_STARTED,
                        progressPercent = 0,
                        isLocked = true
                    ),
                    Lesson(
                        id = "lesson_3_2",
                        unitId = "unit_3",
                        displayName = "Escritura avanzada",
                        description = "Redacción profesional",
                        iconUrl = null,
                        orderIndex = 1,
                        completionState = CompletionState.NOT_STARTED,
                        progressPercent = 0,
                        isLocked = true
                    )
                )
            )
        )

        return LearningPath(
            id = "path_1",
            userId = userId,
            courseId = "course_1",
            courseName = "Náhuatl Básico",
            units = units,
            nextRecommendedLessonId = "lesson_1_3",
            overallProgress = 25
        )
    }

    fun clearCache() {
        cachedLearningPath = null
    }
}
