package com.req.software.amoxcalli_app.domain.model

/**
 * Domain model representing the complete learning path
 */
data class LearningPath(
    val id: String,
    val userId: String,
    val courseId: String,
    val courseName: String,
    val units: List<LearningUnit> = emptyList(),
    val nextRecommendedLessonId: String? = null,
    val overallProgress: Int = 0
)
