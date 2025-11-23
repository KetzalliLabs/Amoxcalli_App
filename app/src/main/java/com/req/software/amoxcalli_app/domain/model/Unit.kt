package com.req.software.amoxcalli_app.domain.model

/**
 * Domain model representing a learning unit containing multiple lessons
 */
data class LearningUnit(
    val id: String,
    val displayName: String,
    val description: String?,
    val orderIndex: Int,
    val lessons: List<Lesson> = emptyList(),
    val isLocked: Boolean = false,
    val guidebookUrl: String? = null
)
