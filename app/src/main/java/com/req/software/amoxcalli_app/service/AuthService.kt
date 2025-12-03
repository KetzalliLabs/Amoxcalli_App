package com.req.software.amoxcalli_app.service

import com.req.software.amoxcalli_app.config.ApiConfig
import com.req.software.amoxcalli_app.data.dto.ApiResponse
import com.req.software.amoxcalli_app.data.dto.LoginRequest
import com.req.software.amoxcalli_app.data.dto.UserRegistrationRequest
import com.req.software.amoxcalli_app.data.dto.UserResponse
import com.req.software.amoxcalli_app.data.dto.UserStatsResponse
import com.req.software.amoxcalli_app.data.dto.ExerciseCompletionRequest
import com.req.software.amoxcalli_app.data.dto.DailyQuizRequest
import com.req.software.amoxcalli_app.data.dto.CategoryProgressRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Auth API Service
 * Defines endpoints for authentication operations
 */
interface AuthService {
    /**
     * Login with Firebase UID
     * @param request Contains the Firebase UID and user info
     * @return User data from backend
     */
    @POST(ApiConfig.Endpoints.LOGIN)
    suspend fun login(@Body request: UserRegistrationRequest): ApiResponse<UserResponse>

    /**
     * Register/sync user with backend
     * @param request Contains user information from Firebase
     * @return User data from backend
     */
    @POST(ApiConfig.Endpoints.REGISTER)
    suspend fun register(@Body request: UserRegistrationRequest): ApiResponse<UserResponse>

    /**
     * Get user stats and progress
     * @param authToken Firebase auth token
     * @return User stats including progress, streak, medals, etc.
     */
    @GET(ApiConfig.Endpoints.USER_STATS)
    suspend fun getUserStats(@Header("Authorization") authToken: String): ApiResponse<UserStatsResponse>

    /**
     * Record a sign view
     * @param authToken Firebase auth token
     * @param signId Sign ID
     */
    @POST("auth/me/signs/{signId}/view")
    suspend fun recordSignView(
        @Header("Authorization") authToken: String,
        @Path("signId") signId: String
    ): ApiResponse<Unit>

    /**
     * Record exercise completion
     * @param authToken Firebase auth token
     * @param exerciseId Exercise ID
     * @param request Exercise completion data
     */
    @POST("auth/me/exercises/{exerciseId}/complete")
    suspend fun recordExerciseCompletion(
        @Header("Authorization") authToken: String,
        @Path("exerciseId") exerciseId: String,
        @Body request: ExerciseCompletionRequest
    ): ApiResponse<Unit>

    /**
     * Record daily quiz result
     * @param authToken Firebase auth token
     * @param request Daily quiz data
     */
    @POST("auth/me/daily-quiz")
    suspend fun recordDailyQuiz(
        @Header("Authorization") authToken: String,
        @Body request: DailyQuizRequest
    ): ApiResponse<Unit>

    /**
     * Update or create category progress
     * @param authToken Firebase auth token
     * @param request Category progress data
     */
    @POST("auth/me/progress")
    suspend fun updateCategoryProgress(
        @Header("Authorization") authToken: String,
        @Body request: CategoryProgressRequest
    ): ApiResponse<Unit>

    /**
     * Add sign to favorites
     * @param authToken Firebase auth token
     * @param signId Sign ID
     */
    @POST("auth/me/favorites/{signId}")
    suspend fun addToFavorites(
        @Header("Authorization") authToken: String,
        @Path("signId") signId: String
    ): ApiResponse<com.req.software.amoxcalli_app.data.dto.FavoriteResponse>

    /**
     * Remove sign from favorites
     * @param authToken Firebase auth token
     * @param signId Sign ID
     */
    @DELETE("auth/me/favorites/{signId}")
    suspend fun removeFromFavorites(
        @Header("Authorization") authToken: String,
        @Path("signId") signId: String
    ): ApiResponse<Unit>

    /**
     * Get user's favorite signs
     * @param authToken Firebase auth token
     * @return List of favorite signs
     */
    @GET("auth/me/favorites")
    suspend fun getFavorites(
        @Header("Authorization") authToken: String
    ): com.req.software.amoxcalli_app.data.dto.ListResponse<com.req.software.amoxcalli_app.data.dto.FavoriteSignDto>

    /**
     * Get all available medals (public)
     * @return List of all medals that can be earned
     */
    @GET("auth/medals")
    suspend fun getAllMedals(): ApiResponse<List<com.req.software.amoxcalli_app.data.dto.MedalInfo>>

    /**
     * Get medals earned by the authenticated user
     * @param authToken Firebase auth token
     * @return List of medals earned by the user
     */
    @GET("auth/me/medals")
    suspend fun getUserMedals(
        @Header("Authorization") authToken: String
    ): ApiResponse<List<com.req.software.amoxcalli_app.data.dto.Medal>>

    /**
     * Claim a medal (user must meet conditions)
     * @param authToken Firebase auth token
     * @param medalId Medal ID to claim
     * @return Success response with medal data
     */
    @POST("auth/me/medals/{medalId}/claim")
    suspend fun claimMedal(
        @Header("Authorization") authToken: String,
        @Path("medalId") medalId: String
    ): ApiResponse<com.req.software.amoxcalli_app.data.dto.MedalClaimResponse>
}
