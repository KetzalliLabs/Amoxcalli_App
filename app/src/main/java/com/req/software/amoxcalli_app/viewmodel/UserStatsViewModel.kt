package com.req.software.amoxcalli_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.req.software.amoxcalli_app.data.dto.UserStatsResponse
import com.req.software.amoxcalli_app.data.local.XPManager
import com.req.software.amoxcalli_app.network.RetrofitClient
import com.req.software.amoxcalli_app.ui.components.notifications.StatsNotificationState
import com.req.software.amoxcalli_app.ui.components.notifications.StatsNotifications
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Centralized ViewModel for user stats management
 * This ViewModel is shared across the app to provide consistent user stats
 * Now includes local XP tracking
 */
class UserStatsViewModel(application: Application) : AndroidViewModel(application) {
    private val authService = RetrofitClient.authService
    private val xpManager = XPManager(application)

    private val _userStats = MutableStateFlow<UserStatsResponse?>(null)
    val userStats: StateFlow<UserStatsResponse?> = _userStats.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Local XP tracking
    private val _localXP = MutableStateFlow(0)
    val localXP: StateFlow<Int> = _localXP.asStateFlow()

    private val _sessionXP = MutableStateFlow(0)
    val sessionXP: StateFlow<Int> = _sessionXP.asStateFlow()

    // Notification state
    private val _notification = MutableStateFlow<StatsNotificationState?>(null)
    val notification: StateFlow<StatsNotificationState?> = _notification.asStateFlow()

    init {
        loadLocalXP()
    }

    /**
     * Load local XP from SharedPreferences
     */
    private fun loadLocalXP() {
        _localXP.value = xpManager.getTotalXP()
        _sessionXP.value = xpManager.getSessionXP()
    }

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

    // ============================================
    // LOCAL XP MANAGEMENT
    // ============================================

    /**
     * Award XP for a correct answer and show notification
     */
    fun awardCorrectAnswerXP() {
        val xpAmount = xpManager.awardCorrectAnswer()
        _localXP.value = xpManager.getTotalXP()
        _sessionXP.value = xpManager.getSessionXP()
        showNotification(StatsNotifications.xpGained(xpAmount))
    }

    /**
     * Award XP for completing an exercise and show notification
     */
    fun awardExerciseCompletionXP() {
        val xpAmount = xpManager.awardExerciseCompletion()
        _localXP.value = xpManager.getTotalXP()
        _sessionXP.value = xpManager.getSessionXP()
        showNotification(StatsNotifications.exerciseCompleted(xpAmount))
    }

    /**
     * Award daily bonus XP and show notification
     */
    fun awardDailyBonusXP() {
        val xpAmount = xpManager.awardDailyBonus()
        _localXP.value = xpManager.getTotalXP()
        _sessionXP.value = xpManager.getSessionXP()
        showNotification(StatsNotifications.dailyBonus(xpAmount))
    }

    /**
     * Add custom XP amount
     */
    fun addXP(amount: Int) {
        xpManager.addXP(amount)
        _localXP.value = xpManager.getTotalXP()
        _sessionXP.value = xpManager.getSessionXP()
        showNotification(StatsNotifications.xpGained(amount))
    }

    /**
     * Reset session XP
     */
    fun resetSessionXP() {
        xpManager.resetSessionXP()
        _sessionXP.value = 0
    }

    /**
     * Set total XP (for testing or sync)
     */
    fun setTotalXP(amount: Int) {
        xpManager.setTotalXP(amount)
        _localXP.value = amount
    }

    /**
     * Reset all XP (for testing)
     */
    fun resetAllXP() {
        xpManager.resetAllXP()
        _localXP.value = 0
        _sessionXP.value = 0
    }

    /**
     * Show a notification
     */
    fun showNotification(notification: StatsNotificationState) {
        _notification.value = notification
    }

    /**
     * Clear notification (called automatically after display)
     */
    fun clearNotification() {
        _notification.value = null
    }
}
