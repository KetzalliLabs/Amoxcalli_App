package com.req.software.amoxcalli_app.domain.usecase

import com.req.software.amoxcalli_app.data.repository.CourseRepository
import com.req.software.amoxcalli_app.domain.model.LearningPath

/**
 * Use case for fetching learning path data
 */
class GetLearningPathUseCase(
    private val courseRepository: CourseRepository = CourseRepository()
) {
    suspend operator fun invoke(userId: String, forceRefresh: Boolean = false): Result<LearningPath> {
        return courseRepository.getLearningPath(userId, forceRefresh)
    }
}
