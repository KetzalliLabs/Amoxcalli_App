package com.req.software.amoxcalli_app.ui.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.req.software.amoxcalli_app.ui.components.headers.StatsHeader
import com.req.software.amoxcalli_app.ui.theme.ThirdColor
import com.req.software.amoxcalli_app.viewmodel.UserStatsViewModel

/**
 * Centralized TopBar that shows user stats consistently across all screens
 * This ensures the same values are displayed everywhere at any given time
 */
@Composable
fun AppTopBar(
    userStatsViewModel: UserStatsViewModel,
    modifier: Modifier = Modifier
) {
    // Get reactive user stats
    val userStats by userStatsViewModel.userStats.collectAsState()

    // Get local XP for real-time updates
    val localXP by userStatsViewModel.localXP.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ThirdColor)
            .padding(top = 12.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Extract values from UserStatsResponse
        val coins = userStats?.stats?.find { it.name == "coins" }?.currentValue ?: 0
        val energy = userStats?.stats?.find { it.name == "energy" }?.currentValue ?: 20
        val streak = userStats?.streak?.currentDays ?: 0
        val experience = userStats?.stats?.find { it.name == "experience_points" }?.currentValue ?: 0

        StatsHeader(
            coins = coins,
            energy = energy,
            streak = streak,
            experience = experience,
            medalsCount = userStats?.medals?.size ?: 0,
            useLocalXP = true,
            localXP = localXP
        )
    }
}

