package com.req.software.amoxcalli_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.req.software.amoxcalli_app.domain.model.UserStats
import com.req.software.amoxcalli_app.data.dto.Medal
import com.req.software.amoxcalli_app.data.dto.UserStatsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Simplified ViewModel for Home screen
 * Manages user stats and medals
 */
class HomeViewModel : ViewModel() {

    private val _userStats = MutableStateFlow(UserStats())
    val userStats: StateFlow<UserStats> = _userStats.asStateFlow()

    private val _medals = MutableStateFlow<List<Medal>>(emptyList())
    val medals: StateFlow<List<Medal>> = _medals.asStateFlow()

    /**
     * Update user stats and medals from UserStatsResponse
     */
    fun updateUserStats(statsResponse: UserStatsResponse?) {
        if (statsResponse != null) {
            val coins = statsResponse.stats?.find { it.name == "coins" }?.currentValue ?: 0
            val energy = statsResponse.stats?.find { it.name == "energy" }?.currentValue ?: 20
            val streak = statsResponse.streak?.currentDays ?: 0
            val experience = statsResponse.stats?.find { it.name == "experience_points" }?.currentValue ?: 0

            _userStats.value = UserStats(
                coins = coins,
                energy = energy,
                streak = streak,
                experience = experience
            )

            // Update medals
            _medals.value = statsResponse.medals ?: emptyList()
        }
    }
}
