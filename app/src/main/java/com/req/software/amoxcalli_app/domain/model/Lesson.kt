package com.req.software.amoxcalli_app.domain.model

/**
 * Domain model representing a single lesson/skill
 */
data class Lesson(
    val id: String,
    val unitId: String,
    val displayName: String,
    val description: String?,
    val iconUrl: String?,
    val orderIndex: Int,
    val completionState: CompletionState,
    val progressPercent: Int = 0,
    val isLocked: Boolean = false,
    val isCheckpoint: Boolean = false,
    val estimatedDuration: Int = 10, // minutes
    val type: LessonType = LessonType.STANDARD,
    val starsEarned: Int = 0,
    val maxStars: Int = 3
)

enum class CompletionState {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED
}

enum class LessonType {
    STANDARD,
    STORY,
    PRACTICE,
    CHECKPOINT
}
