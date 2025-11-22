package com.req.software.amoxcalli_app.service

import com.req.software.amoxcalli_app.data.dto.ApiResponse
import com.req.software.amoxcalli_app.data.dto.LearningPathDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit service for course and learning path API calls
 */
interface CourseService {
    @GET("users/{userId}/learning-path")
    suspend fun getLearningPath(@Path("userId") userId: String): ApiResponse<LearningPathDto>
}
