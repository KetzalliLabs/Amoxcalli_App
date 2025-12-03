package com.req.software.amoxcalli_app.ui.components.medals

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.req.software.amoxcalli_app.data.dto.UserStatsResponse
import com.req.software.amoxcalli_app.viewmodel.MedalViewModel

/**
 * Composable that checks for medal achievements and shows celebration
 * Add this to your app's top-level navigation to monitor medals globally
 */
@Composable
fun MedalChecker(
    userStats: UserStatsResponse?,
    authToken: String?,
    medalViewModel: MedalViewModel = viewModel()
) {
    val newlyEarnedMedal by medalViewModel.newlyEarnedMedal.collectAsState()

    // Check for new medals when user stats change
    LaunchedEffect(userStats, authToken) {
        if (userStats != null && authToken != null) {
            medalViewModel.checkAndClaimMedals(userStats, authToken)
        }
    }

    // Load user medals on init
    LaunchedEffect(authToken) {
        if (authToken != null) {
            medalViewModel.loadUserMedals(authToken)
        }
    }

    // Show celebration dialog when new medal is earned
    newlyEarnedMedal?.let { medal ->
        MedalCelebrationDialog(
            medal = medal,
            onDismiss = {
                medalViewModel.dismissMedalCelebration()
            }
        )
    }
}
