package com.req.software.amoxcalli_app.data.dto

import com.google.gson.annotations.SerializedName
import com.req.software.amoxcalli_app.domain.model.*

/**
 * DTO for learning path from API
 */
data class LearningPathDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("course_id")
    val courseId: String,

    @SerializedName("course_name")
    val courseName: String,

    @SerializedName("units")
    val units: List<UnitDto>,

    @SerializedName("next_recommended_lesson_id")
    val nextRecommendedLessonId: String?,

    @SerializedName("overall_progress")
    val overallProgress: Int = 0
)

data class UnitDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("display_name")
    val displayName: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("order_index")
    val orderIndex: Int,

    @SerializedName("lessons")
    val lessons: List<LessonDto>,

    @SerializedName("is_locked")
    val isLocked: Boolean = false,

    @SerializedName("guidebook_url")
    val guidebookUrl: String?
)

data class LessonDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("unit_id")
    val unitId: String,

    @SerializedName("display_name")
    val displayName: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("icon_url")
    val iconUrl: String?,

    @SerializedName("order_index")
    val orderIndex: Int,

    @SerializedName("completion_state")
    val completionState: String,

    @SerializedName("progress_percent")
    val progressPercent: Int = 0,

    @SerializedName("is_locked")
    val isLocked: Boolean = false,

    @SerializedName("is_checkpoint")
    val isCheckpoint: Boolean = false,

    @SerializedName("estimated_duration")
    val estimatedDuration: Int = 10,

    @SerializedName("type")
    val type: String = "STANDARD",

    @SerializedName("stars_earned")
    val starsEarned: Int = 0,

    @SerializedName("max_stars")
    val maxStars: Int = 3
)

/**
 * DTO for user profile from API
 */
data class UserProfileDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("firebase_uid")
    val firebaseUid: String,

    @SerializedName("display_name")
    val displayName: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("avatar_url")
    val avatarUrl: String?,

    @SerializedName("level")
    val level: Int = 1,

    @SerializedName("streak_count")
    val streakCount: Int = 0,

    @SerializedName("experience_points")
    val experiencePoints: Int = 0,

    @SerializedName("coins")
    val coins: Int = 0,

    @SerializedName("premium_currency")
    val premiumCurrency: Int = 0,

    @SerializedName("join_date")
    val joinDate: String
)

// Mapper functions
fun LearningPathDto.toDomain(): LearningPath = LearningPath(
    id = id,
    userId = userId,
    courseId = courseId,
    courseName = courseName,
    units = units.map { it.toDomain() },
    nextRecommendedLessonId = nextRecommendedLessonId,
    overallProgress = overallProgress
)

fun UnitDto.toDomain(): LearningUnit = LearningUnit(
    id = id,
    displayName = displayName,
    description = description,
    orderIndex = orderIndex,
    lessons = lessons.map { it.toDomain() },
    isLocked = isLocked,
    guidebookUrl = guidebookUrl
)

fun LessonDto.toDomain(): Lesson = Lesson(
    id = id,
    unitId = unitId,
    displayName = displayName,
    description = description,
    iconUrl = iconUrl,
    orderIndex = orderIndex,
    completionState = when (completionState.uppercase()) {
        "IN_PROGRESS" -> CompletionState.IN_PROGRESS
        "COMPLETED" -> CompletionState.COMPLETED
        else -> CompletionState.NOT_STARTED
    },
    progressPercent = progressPercent,
    isLocked = isLocked,
    isCheckpoint = isCheckpoint,
    estimatedDuration = estimatedDuration,
    type = when (type.uppercase()) {
        "STORY" -> LessonType.STORY
        "PRACTICE" -> LessonType.PRACTICE
        "CHECKPOINT" -> LessonType.CHECKPOINT
        else -> LessonType.STANDARD
    },
    starsEarned = starsEarned,
    maxStars = maxStars
)

fun UserProfileDto.toDomain(): UserProfile = UserProfile(
    id = id,
    firebaseUid = firebaseUid,
    displayName = displayName,
    email = email,
    avatarUrl = avatarUrl,
    level = level,
    streakCount = streakCount,
    experiencePoints = experiencePoints,
    coins = coins,
    premiumCurrency = premiumCurrency,
    joinDate = joinDate
)
