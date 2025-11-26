package com.req.software.amoxcalli_app.service

import com.req.software.amoxcalli_app.data.dto.ExerciseDto
import com.req.software.amoxcalli_app.data.dto.ListResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Exercises API Service
 * Handles exercise/quiz data
 */
interface ExercisesService {
    /**
     * Get exercises
     * @param categoryId Optional category filter
     * @param exerciseId Optional specific exercise ID
     * @return List of exercises with options
     */
    @GET("items/exercises")
    suspend fun getExercises(
        @Query("category_id") categoryId: String? = null,
        @Query("exercise_id") exerciseId: String? = null
    ): ListResponse<ExerciseDto>
}
