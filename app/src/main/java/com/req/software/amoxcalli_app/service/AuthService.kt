package com.req.software.amoxcalli_app.service

import com.req.software.amoxcalli_app.config.ApiConfig
import com.req.software.amoxcalli_app.data.dto.ApiResponse
import com.req.software.amoxcalli_app.data.dto.LoginRequest
import com.req.software.amoxcalli_app.data.dto.UserRegistrationRequest
import com.req.software.amoxcalli_app.data.dto.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Auth API Service
 * Defines endpoints for authentication operations
 */
interface AuthService {
    /**
     * Login with Google ID token
     * @param request Contains the Google ID token and user info
     * @return User data from backend
     */
    @POST(ApiConfig.Endpoints.LOGIN)
    suspend fun login(@Body request: LoginRequest): ApiResponse<UserResponse>

    /**
     * Register/sync user with backend
     * @param request Contains user information from Firebase
     * @return User data from backend
     */
    @POST(ApiConfig.Endpoints.REGISTER)
    suspend fun register(@Body request: UserRegistrationRequest): ApiResponse<UserResponse>
}
