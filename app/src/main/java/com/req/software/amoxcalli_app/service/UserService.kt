package com.req.software.amoxcalli_app.service

import com.req.software.amoxcalli_app.data.dto.ApiResponse
import com.req.software.amoxcalli_app.data.dto.UserProfileDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit service for user-related API calls
 */
interface UserService {
    @GET("users/{userId}/profile")
    suspend fun getUserProfile(@Path("userId") userId: String): ApiResponse<UserProfileDto>
}
