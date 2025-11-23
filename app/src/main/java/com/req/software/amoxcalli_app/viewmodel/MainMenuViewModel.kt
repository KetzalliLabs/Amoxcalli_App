package com.req.software.amoxcalli_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.req.software.amoxcalli_app.analytics.AnalyticsTracker
import com.req.software.amoxcalli_app.analytics.LogAnalyticsTracker
import com.req.software.amoxcalli_app.domain.model.LearningPath
import com.req.software.amoxcalli_app.domain.model.Lesson
import com.req.software.amoxcalli_app.domain.model.UserProfile
import com.req.software.amoxcalli_app.domain.usecase.GetLearningPathUseCase
import com.req.software.amoxcalli_app.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the main menu screen
 * Manages user profile, learning path data, and UI state
 */
class MainMenuViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase = GetUserProfileUseCase(),
    private val getLearningPathUseCase: GetLearningPathUseCase = GetLearningPathUseCase(),
    private val analyticsTracker: AnalyticsTracker = LogAnalyticsTracker()
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainMenuUiState>(MainMenuUiState.Loading)
    val uiState: StateFlow<MainMenuUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent.asStateFlow()

    private var currentUserId: String? = null

    /**
     * Initialize and load main menu data
     */
    fun loadMainMenu(userId: String) {
        currentUserId = userId
        analyticsTracker.trackScreenView("main_menu", mapOf("userId" to userId))
        loadData(forceRefresh = false)
    }

    /**
     * Refresh data on user request
     */
    fun onRefreshRequested() {
        analyticsTracker.trackInteraction("refresh_main_menu")
        loadData(forceRefresh = true)
    }

    /**
     * Handle lesson click
     */
    fun onLessonClicked(lessonId: String, lessonName: String) {
        analyticsTracker.trackInteraction(
            "lesson_clicked",
            mapOf("lessonId" to lessonId, "lessonName" to lessonName)
        )
        _navigationEvent.value = NavigationEvent.NavigateToLesson(lessonId)
    }

    /**
     * Handle next recommended lesson click (FAB)
     */
    fun onNextLessonClicked() {
        val state = _uiState.value
        if (state is MainMenuUiState.Success && state.nextRecommendedLessonId != null) {
            analyticsTracker.trackInteraction(
                "next_lesson_clicked",
                mapOf("lessonId" to state.nextRecommendedLessonId)
            )
            _navigationEvent.value = NavigationEvent.NavigateToLesson(state.nextRecommendedLessonId)
        }
    }

    /**
     * Handle shop click
     */
    fun onShopClicked() {
        analyticsTracker.trackInteraction("shop_clicked")
        _navigationEvent.value = NavigationEvent.NavigateToShop
    }

    /**
     * Handle profile click
     */
    fun onProfileClicked() {
        analyticsTracker.trackInteraction("profile_clicked")
        _navigationEvent.value = NavigationEvent.NavigateToProfile
    }

    /**
     * Clear navigation event after handling
     */
    fun onNavigationHandled() {
        _navigationEvent.value = null
    }

    /**
     * Load user profile and learning path data
     */
    private fun loadData(forceRefresh: Boolean) {
        val userId = currentUserId ?: return

        viewModelScope.launch {
            _uiState.value = MainMenuUiState.Loading

            try {
                val profileResult = getUserProfileUseCase(userId, forceRefresh)
                val learningPathResult = getLearningPathUseCase(userId, forceRefresh)

                if (profileResult.isSuccess && learningPathResult.isSuccess) {
                    val profile = profileResult.getOrThrow()
                    val learningPath = learningPathResult.getOrThrow()

                    _uiState.value = MainMenuUiState.Success(
                        userProfile = profile,
                        learningPath = learningPath,
                        nextRecommendedLessonId = learningPath.nextRecommendedLessonId
                    )
                } else {
                    val error = profileResult.exceptionOrNull() ?: learningPathResult.exceptionOrNull()
                    _uiState.value = MainMenuUiState.Error(
                        message = error?.message ?: "Error al cargar los datos",
                        cachedData = (_uiState.value as? MainMenuUiState.Success)
                    )
                }
            } catch (e: Exception) {
                _uiState.value = MainMenuUiState.Error(
                    message = e.message ?: "Error desconocido",
                    cachedData = (_uiState.value as? MainMenuUiState.Success)
                )
            }
        }
    }
}

/**
 * UI state for the main menu screen
 */
sealed class MainMenuUiState {
    object Loading : MainMenuUiState()

    data class Success(
        val userProfile: UserProfile,
        val learningPath: LearningPath,
        val nextRecommendedLessonId: String?
    ) : MainMenuUiState()

    data class Error(
        val message: String,
        val cachedData: Success? = null
    ) : MainMenuUiState()
}

/**
 * One-shot navigation events
 */
sealed class NavigationEvent {
    data class NavigateToLesson(val lessonId: String) : NavigationEvent()
    object NavigateToShop : NavigationEvent()
    object NavigateToProfile : NavigationEvent()
}
