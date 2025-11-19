package com.req.software.amoxcalli_app.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Request body for login with Google ID token
 */
data class LoginRequest(
    @SerializedName("id_token")
    val idToken: String,

    @SerializedName("email")
    val email: String?,

    @SerializedName("display_name")
    val displayName: String?,

    @SerializedName("avatar_url")
    val avatarUrl: String?
)

/**
 * Request body for user registration/sync
 */
data class UserRegistrationRequest(
    @SerializedName("firebase_uid")
    val firebaseUid: String,

    @SerializedName("email")
    val email: String?,

    @SerializedName("display_name")
    val displayName: String?,

    @SerializedName("avatar_url")
    val avatarUrl: String?
)

/**
 * User data from backend
 */
data class UserResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("firebase_uid")
    val firebaseUid: String,

    @SerializedName("role_id")
    val roleId: String?,

    @SerializedName("display_name")
    val displayName: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("coin")
    val coin: Int,

    @SerializedName("avatar_url")
    val avatarUrl: String?,

    @SerializedName("join_date")
    val joinDate: String
)

/**
 * Generic API response wrapper
 */
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: T?
)