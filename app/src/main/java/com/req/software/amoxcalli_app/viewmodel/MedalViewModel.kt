package com.req.software.amoxcalli_app.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.req.software.amoxcalli_app.data.dto.Medal
import com.req.software.amoxcalli_app.data.dto.MedalClaimResponse
import com.req.software.amoxcalli_app.data.dto.MedalInfo
import com.req.software.amoxcalli_app.data.dto.UserStatsResponse
import com.req.software.amoxcalli_app.data.local.StreakManager
import com.req.software.amoxcalli_app.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing medals and achievements
 * Handles medal checking, claiming, and celebration UI
 * Uses local streak tracking for racha medals
 */
class MedalViewModel(application: Application) : AndroidViewModel(application) {
    private val authService = RetrofitClient.authService
    private val streakManager = StreakManager(application)

    // All available medals
    private val _availableMedals = MutableStateFlow<List<MedalInfo>>(emptyList())
    val availableMedals: StateFlow<List<MedalInfo>> = _availableMedals.asStateFlow()

    // User's earned medals
    private val _userMedals = MutableStateFlow<List<Medal>>(emptyList())
    val userMedals: StateFlow<List<Medal>> = _userMedals.asStateFlow()

    // Newly earned medal to show celebration
    private val _newlyEarnedMedal = MutableStateFlow<MedalClaimResponse?>(null)
    val newlyEarnedMedal: StateFlow<MedalClaimResponse?> = _newlyEarnedMedal.asStateFlow()

    // Queue for testing medals
    private val medalTestQueue = mutableListOf<MedalClaimResponse>()

    // Local streak tracking
    private val _currentStreak = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()

    private val _bestStreak = MutableStateFlow(0)
    val bestStreak: StateFlow<Int> = _bestStreak.asStateFlow()

    // Loading states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Medal definitions from CSV with IDs
    companion object {
        const val MEDAL_TLAOLLI = "ed9dbe08-a37b-4ecb-a550-0616bf665df3" // 3 días de racha
        const val MEDAL_JADE = "26d2e63e-8066-43fd-ae0e-292b58ec2c64" // 7 días de racha
        const val MEDAL_OBSIDIANA = "f79bd6c8-ae5b-41bb-8d65-3ded14e5c846" // Practica 5 señas
        const val MEDAL_TURQUESA = "ad914cf2-cc0f-42b0-8c45-8e3d2d514b28" // Obtén 100 de XP
        const val MEDAL_QUETZAL = "c17ce2c2-c820-44b4-818f-d602bd95efee" // Completa 3 ejercicios
        const val MEDAL_CODICE_DORADO = "9725bcbb-ffdf-45b5-adf4-78c88af2a75a" // Completa 5 ejercicios
    }

    init {
        loadAvailableMedals()
        updateLocalStreak()
    }

    /**
     * Update local streak and check for today
     * Call this when app opens
     */
    fun updateLocalStreak() {
        val newStreak = streakManager.checkAndUpdateStreak()
        _currentStreak.value = newStreak
        _bestStreak.value = streakManager.getBestStreak()
        Log.d("MedalViewModel", "Local streak updated: $newStreak (best: ${_bestStreak.value})")
    }

    /**
     * Load all available medals (public endpoint)
     */
    fun loadAvailableMedals() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val response = authService.getAllMedals()
                if (response.success && response.data != null) {
                    _availableMedals.value = response.data
                    Log.d("MedalViewModel", "Loaded ${response.data.size} available medals")
                } else {
                    _error.value = response.message ?: "Failed to load medals"
                    Log.e("MedalViewModel", "Failed to load medals: ${response.message}")
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
                Log.e("MedalViewModel", "Error loading medals", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Load user's earned medals
     */
    fun loadUserMedals(authToken: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val response = authService.getUserMedals("Bearer $authToken")
                if (response.success && response.data != null) {
                    _userMedals.value = response.data
                    Log.d("MedalViewModel", "User has ${response.data.size} medals")
                } else {
                    _error.value = response.message ?: "Failed to load user medals"
                    Log.e("MedalViewModel", "Failed to load user medals: ${response.message}")
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
                Log.e("MedalViewModel", "Error loading user medals", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Check user stats and attempt to claim eligible medals
     * Uses LOCAL streak tracking instead of backend
     */
    fun checkAndClaimMedals(userStats: UserStatsResponse, authToken: String) {
        viewModelScope.launch {
            val currentMedalIds = _userMedals.value.map { it.medalId }.toSet()

            // Extract stats (using LOCAL streak, not backend)
            val streak = _currentStreak.value // Use local streak!
            val xp = userStats.stats?.find { it.name == "experience_points" }?.currentValue ?: 0
            val exercisesCompleted = userStats.exerciseHistory?.size ?: 0
            val signsViewed = userStats.signViews?.size ?: 0

            Log.d("MedalViewModel", "Checking medals - Streak: $streak, XP: $xp, Exercises: $exercisesCompleted, Signs: $signsViewed")

            // Check each medal condition
            val medalsToCheck = mutableListOf<String>()

            // Tlaolli - 3 day streak
            if (streak >= 3 && !currentMedalIds.contains(MEDAL_TLAOLLI)) {
                medalsToCheck.add(MEDAL_TLAOLLI)
            }

            // Jade - 7 day streak
            if (streak >= 7 && !currentMedalIds.contains(MEDAL_JADE)) {
                medalsToCheck.add(MEDAL_JADE)
            }

            // Obsidiana - Practice 5 signs
            if (signsViewed >= 5 && !currentMedalIds.contains(MEDAL_OBSIDIANA)) {
                medalsToCheck.add(MEDAL_OBSIDIANA)
            }

            // Turquesa - Get 100 XP
            if (xp >= 100 && !currentMedalIds.contains(MEDAL_TURQUESA)) {
                medalsToCheck.add(MEDAL_TURQUESA)
            }

            // Quetzal - Complete 3 exercises
            if (exercisesCompleted >= 3 && !currentMedalIds.contains(MEDAL_QUETZAL)) {
                medalsToCheck.add(MEDAL_QUETZAL)
            }

            // Códice dorado - Complete 5 exercises
            if (exercisesCompleted >= 5 && !currentMedalIds.contains(MEDAL_CODICE_DORADO)) {
                medalsToCheck.add(MEDAL_CODICE_DORADO)
            }

            // Attempt to claim each medal
            medalsToCheck.forEach { medalId ->
                claimMedal(medalId, authToken)
            }
        }
    }

    /**
     * Claim a specific medal
     */
    private suspend fun claimMedal(medalId: String, authToken: String) {
        try {
            Log.d("MedalViewModel", "Attempting to claim medal: $medalId")
            val response = authService.claimMedal("Bearer $authToken", medalId)

            if (response.success && response.data != null) {
                Log.d("MedalViewModel", "Successfully claimed medal: ${response.data.name}")

                // Add to user medals
                val newMedal = Medal(
                    id = response.data.userMedalId,
                    medalId = response.data.medalId,
                    name = response.data.name,
                    description = response.data.description,
                    iconUrl = response.data.iconUrl,
                    achievedAt = response.data.achievedAt
                )
                _userMedals.value = _userMedals.value + newMedal

                // Trigger celebration UI
                _newlyEarnedMedal.value = response.data
            } else {
                Log.d("MedalViewModel", "Medal not yet earned or already claimed: ${response.message}")
            }
        } catch (e: Exception) {
            Log.e("MedalViewModel", "Error claiming medal $medalId", e)
        }
    }

    /**
     * Dismiss the medal celebration dialog
     * If testing, shows next medal in queue
     */
    fun dismissMedalCelebration() {
        _newlyEarnedMedal.value = null

        // Check if there are more medals in the test queue
        if (medalTestQueue.isNotEmpty()) {
            val nextMedal = medalTestQueue.removeAt(0)
            _newlyEarnedMedal.value = nextMedal
            Log.d("MedalViewModel", "Showing next test medal: ${nextMedal.name}")
        }
    }

    /**
     * Check if user has a specific medal
     */
    fun hasMedal(medalId: String): Boolean {
        return _userMedals.value.any { it.medalId == medalId }
    }

    /**
     * TEST ONLY - Cycle through all medal animations for SUPERADMIN testing
     * Shows each medal celebration sequentially when user dismisses
     */
    fun testAllMedalAnimations() {
        Log.d("MedalViewModel", "Starting medal animation test - 6 medals")

        val testMedals = listOf(
            MedalClaimResponse(
                userMedalId = "test-tlaolli",
                medalId = MEDAL_TLAOLLI,
                name = "Tlaolli",
                description = "3 días de racha",
                iconUrl = "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/M_Tlaolli.png",
                achievedAt = "2025-12-01T00:00:00.000Z"
            ),
            MedalClaimResponse(
                userMedalId = "test-jade",
                medalId = MEDAL_JADE,
                name = "Jade",
                description = "7 días de racha",
                iconUrl = "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/M_Jade.png",
                achievedAt = "2025-12-01T00:00:00.000Z"
            ),
            MedalClaimResponse(
                userMedalId = "test-obsidiana",
                medalId = MEDAL_OBSIDIANA,
                name = "Obsidiana",
                description = "Practica 5 señas",
                iconUrl = "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/M_Obsidiana.png",
                achievedAt = "2025-12-01T00:00:00.000Z"
            ),
            MedalClaimResponse(
                userMedalId = "test-turquesa",
                medalId = MEDAL_TURQUESA,
                name = "Turquesa",
                description = "Obtén 100 de XP",
                iconUrl = "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/M_Turquesa.png",
                achievedAt = "2025-12-01T00:00:00.000Z"
            ),
            MedalClaimResponse(
                userMedalId = "test-quetzal",
                medalId = MEDAL_QUETZAL,
                name = "Quetzal",
                description = "Completa 3 ejercicios",
                iconUrl = "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/M_Quetzal.png",
                achievedAt = "2025-12-01T00:00:00.000Z"
            ),
            MedalClaimResponse(
                userMedalId = "test-codice",
                medalId = MEDAL_CODICE_DORADO,
                name = "Códice dorado",
                description = "Completa 5 ejercicios",
                iconUrl = "https://pub-05700fc259bc4e839552241871f5e896.r2.dev/M_Oro.png",
                achievedAt = "2025-12-01T00:00:00.000Z"
            )
        )

        // Clear existing queue and populate with test medals
        medalTestQueue.clear()
        medalTestQueue.addAll(testMedals.drop(1)) // Add all but the first

        // Show the first medal immediately
        _newlyEarnedMedal.value = testMedals.first()
        Log.d("MedalViewModel", "Showing first test medal: ${testMedals.first().name}")
        // Remaining medals will show automatically when user dismisses via the queue
    }

    /**
     * Get progress towards a medal
     * Uses LOCAL streak for racha medals
     */
    fun getMedalProgress(medalId: String, userStats: UserStatsResponse): Pair<Int, Int> {
        val streak = _currentStreak.value // Use local streak!
        val xp = userStats.stats?.find { it.name == "experience_points" }?.currentValue ?: 0
        val exercisesCompleted = userStats.exerciseHistory?.size ?: 0
        val signsViewed = userStats.signViews?.size ?: 0

        return when (medalId) {
            MEDAL_TLAOLLI -> Pair(streak, 3)
            MEDAL_JADE -> Pair(streak, 7)
            MEDAL_OBSIDIANA -> Pair(signsViewed, 5)
            MEDAL_TURQUESA -> Pair(xp, 100)
            MEDAL_QUETZAL -> Pair(exercisesCompleted, 3)
            MEDAL_CODICE_DORADO -> Pair(exercisesCompleted, 5)
            else -> Pair(0, 1)
        }
    }

    /**
     * Reset streak (for testing or user request)
     */
    fun resetStreak() {
        streakManager.resetStreak()
        _currentStreak.value = 0
        _bestStreak.value = 0
        Log.d("MedalViewModel", "Streak reset")
    }

    /**
     * Set streak for testing (SUPERADMIN only)
     */
    fun setStreakForTesting(days: Int) {
        streakManager.setStreakForTesting(days)
        _currentStreak.value = days
        Log.d("MedalViewModel", "Streak set to $days for testing")
    }
}
