package com.req.software.amoxcalli_app.data.repository

import com.req.software.amoxcalli_app.domain.model.UserProfile
import kotlinx.coroutines.delay

/**
 * Repository for user profile data
 * MVP: Uses in-memory cache with mock data fallback
 */
class UserRepository {
    private var cachedProfile: UserProfile? = null

    suspend fun getUserProfile(userId: String, forceRefresh: Boolean = false): Result<UserProfile> {
        return try {
            if (!forceRefresh && cachedProfile != null) {
                return Result.success(cachedProfile!!)
            }

            delay(500)

            val profile = UserProfile(
                id = userId,
                firebaseUid = userId,
                displayName = "Estudiante",
                email = "estudiante@amoxcalli.com",
                avatarUrl = "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/KetzalliLabsLogo.jpg",
                level = 5,
                streakCount = 7,
                experiencePoints = 1250,
                coins = 500,
                premiumCurrency = 25,
                joinDate = "2024-01-15"
            )

            cachedProfile = profile
            Result.success(profile)
        } catch (e: Exception) {
            if (cachedProfile != null) {
                Result.success(cachedProfile!!)
            } else {
                Result.failure(e)
            }
        }
    }

    fun clearCache() {
        cachedProfile = null
    }
}
