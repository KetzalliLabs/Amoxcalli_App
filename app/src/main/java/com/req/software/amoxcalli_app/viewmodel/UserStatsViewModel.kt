package com.req.software.amoxcalli_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.req.software.amoxcalli_app.data.dto.UserStatsResponse
import com.req.software.amoxcalli_app.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Centralized ViewModel for user stats management
 * This ViewModel is shared across the app to provide consistent user stats
 */
class UserStatsViewModel : ViewModel() {
    private val authService = RetrofitClient.authService

    private val _userStats = MutableStateFlow<UserStatsResponse?>(null)
    val userStats: StateFlow<UserStatsResponse?> = _userStats.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * Load user stats from backend
     * @param authToken Firebase auth token (format: "Bearer <token>")
     */
    fun loadUserStats(authToken: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val response = authService.getUserStats(authToken)

                if (response.success && response.data != null) {
                    _userStats.value = response.data
                } else {
                    _error.value = response.message ?: "Failed to load user stats"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
                // Keep existing stats if available
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Refresh user stats
     */
    fun refreshStats(authToken: String) {
        loadUserStats(authToken)
    }

    /**
     * Clear user stats (for logout)
     */
    fun clearStats() {
        _userStats.value = null
        _error.value = null
    }

    /**
     * Get exercises completed count
     */
    fun getExercisesCompleted(): Int {
        return _userStats.value?.stats?.find { it.name == "exercises_completed" }?.currentValue ?: 0
    }

    /**
     * Get current streak
     */
    fun getCurrentStreak(): Int {
        return _userStats.value?.streak?.currentDays ?: 0
    }

    /**
     * Get best streak
     */
    fun getBestStreak(): Int {
        return _userStats.value?.streak?.bestDays ?: 0
    }

    /**
     * Get accuracy percentage
     */
    fun getAccuracyPercentage(): Double {
        return _userStats.value?.attempts?.accuracyPercentage ?: 0.0
    }

    /**
     * Get total attempts
     */
    fun getTotalAttempts(): Int {
        return _userStats.value?.attempts?.total ?: 0
    }

    /**
     * Get correct attempts
     */
    fun getCorrectAttempts(): Int {
        return _userStats.value?.attempts?.correct ?: 0
    }

    /**
     * Get medals count
     */
    fun getMedalsCount(): Int {
        return _userStats.value?.medals?.size ?: 0
    }

    /**
     * Get categories in progress
     */
    fun getCategoriesInProgress(): Int {
        return _userStats.value?.progress?.count { it.status == "in_progress" } ?: 0
    }

    /**
     * Get categories completed
     */
    fun getCategoriesCompleted(): Int {
        return _userStats.value?.progress?.count { it.status == "completed" } ?: 0
    }
}
