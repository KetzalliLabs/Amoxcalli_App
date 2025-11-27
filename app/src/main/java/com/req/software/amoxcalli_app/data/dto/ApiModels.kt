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
    val coin: Int? = 0,

    @SerializedName("avatar_url")
    val avatarUrl: String?,

    @SerializedName("join_date")
    val joinDate: String? = null
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

/**
 * User stats response from /api/auth/me/stats
 */
data class UserStatsResponse(
    @SerializedName("user")
    val user: UserInfo,

    @SerializedName("progress")
    val progress: List<CategoryProgress>? = emptyList(),

    @SerializedName("streak")
    val streak: Streak,

    @SerializedName("medals")
    val medals: List<Medal>? = emptyList(),

    @SerializedName("stats")
    val stats: List<Stat>? = emptyList(),

    @SerializedName("attempts")
    val attempts: Attempts,

    @SerializedName("daily_quiz_history")
    val dailyQuizHistory: List<DailyQuiz>? = emptyList(),

    @SerializedName("exercise_history")
    val exerciseHistory: List<ExerciseHistory>? = emptyList(),

    @SerializedName("sign_views")
    val signViews: List<SignView>? = emptyList()
)

data class UserInfo(
    @SerializedName("id")
    val id: String,

    @SerializedName("firebase_uid")
    val firebaseUid: String,

    @SerializedName("display_name")
    val displayName: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("avatar_url")
    val avatarUrl: String?
)

data class CategoryProgress(
    @SerializedName("category_id")
    val categoryId: String,

    @SerializedName("score")
    val score: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("updated_at")
    val updatedAt: String
)

data class Streak(
    @SerializedName("current_days")
    val currentDays: Int = 0,

    @SerializedName("best_days")
    val bestDays: Int = 0,

    @SerializedName("last_check")
    val lastCheck: String?
)

data class Medal(
    @SerializedName("id")
    val id: String,

    @SerializedName("medal_id")
    val medalId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("icon_url")
    val iconUrl: String?,

    @SerializedName("achieved_at")
    val achievedAt: String
)

data class Stat(
    @SerializedName("stat_id")
    val statId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("current_value")
    val currentValue: Int,

    @SerializedName("max_value")
    val maxValue: Int?,

    @SerializedName("last_update")
    val lastUpdate: String
)

data class Attempts(
    @SerializedName("total")
    val total: Int = 0,

    @SerializedName("correct")
    val correct: Int = 0,

    @SerializedName("accuracy_percentage")
    val accuracyPercentage: Double = 0.0
)

data class DailyQuiz(
    @SerializedName("date")
    val date: String,

    @SerializedName("score")
    val score: Int,

    @SerializedName("completed")
    val completed: Boolean
)

data class ExerciseHistory(
    @SerializedName("exercise_id")
    val exerciseId: String,

    @SerializedName("completed_at")
    val completedAt: String
)

data class SignView(
    @SerializedName("sign_id")
    val signId: String,

    @SerializedName("viewed_at")
    val viewedAt: String
)

/**
 * Sign/Word data from /api/items?type=signs
 */
data class SignDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("category_id")
    val categoryId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("video_url")
    val videoUrl: String?
)

/**
 * Category data from /api/items?type=categories
 */
data class CategoryDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("icon_url")
    val iconUrl: String?
)

/**
 * Exercise data from /api/items/exercises
 */
data class ExerciseDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("category_id")
    val categoryId: String,

    @SerializedName("category_name")
    val categoryName: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("question")
    val question: String,

    @SerializedName("correct_sign_id")
    val correctSignId: String,

    @SerializedName("correct_sign")
    val correctSign: SignDto,

    @SerializedName("options")
    val options: List<ExerciseOptionDto>
)

/**
 * Exercise option data
 */
data class ExerciseOptionDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("video_url")
    val videoUrl: String?,

    @SerializedName("is_correct")
    val isCorrect: Boolean
)

/**
 * Request to record exercise completion
 */
data class ExerciseCompletionRequest(
    @SerializedName("isCorrect")
    val isCorrect: Boolean,

    @SerializedName("selectedSignId")
    val selectedSignId: String,

    @SerializedName("timeTaken")
    val timeTaken: Long,

    @SerializedName("score")
    val score: Int
)

/**
 * Request to record daily quiz
 */
data class DailyQuizRequest(
    @SerializedName("date")
    val date: String,

    @SerializedName("numQuestions")
    val numQuestions: Int,

    @SerializedName("correctCount")
    val correctCount: Int,

    @SerializedName("score")
    val score: Int,

    @SerializedName("completed")
    val completed: Boolean
)

/**
 * List response wrapper with count
 */
data class ListResponse<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("count")
    val count: Int,

    @SerializedName("data")
    val data: List<T>
)

/**
 * Request to update category progress
 */
data class CategoryProgressRequest(
    @SerializedName("category_id")
    val categoryId: String,

    @SerializedName("score")
    val score: Int,

    @SerializedName("status")
    val status: String // "in_progress", "completed", etc.
)