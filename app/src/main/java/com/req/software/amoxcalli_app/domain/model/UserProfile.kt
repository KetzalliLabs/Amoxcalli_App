package com.req.software.amoxcalli_app.domain.model

/**
 * Domain model representing user profile with learning progress
 */
data class UserProfile(
    val id: String,
    val firebaseUid: String,
    val displayName: String?,
    val email: String?,
    val avatarUrl: String?,
    val level: Int = 1,
    val streakCount: Int = 0,
    val experiencePoints: Int = 0,
    val coins: Int = 0,
    val premiumCurrency: Int = 0,
    val joinDate: String
)
