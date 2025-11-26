package com.req.software.amoxcalli_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.req.software.amoxcalli_app.domain.model.Topic
import com.req.software.amoxcalli_app.domain.model.UserStats
import com.req.software.amoxcalli_app.data.dto.UserStatsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla Home
 * Maneja el estado y lógica de negocio de la pantalla principal
 */
class HomeViewModel : ViewModel() {

    private val _userStats = MutableStateFlow(UserStats())
    val userStats: StateFlow<UserStats> = _userStats.asStateFlow()

    private val _recentTopics = MutableStateFlow<List<Topic>>(emptyList())
    val recentTopics: StateFlow<List<Topic>> = _recentTopics.asStateFlow()

    private val _recommendedTopics = MutableStateFlow<List<Topic>>(emptyList())
    val recommendedTopics: StateFlow<List<Topic>> = _recommendedTopics.asStateFlow()

    /**
     * Update user stats from UserStatsResponse
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

            // Load topics based on progress
            loadTopicsFromProgress(statsResponse)
        }
    }

    /**
     * Load topics based on user progress
     */
    private fun loadTopicsFromProgress(statsResponse: UserStatsResponse) {
        viewModelScope.launch {
            val progress = statsResponse.progress ?: emptyList()

            // Map progress to topics (using category IDs)
            val recentTopicsList = progress.take(3).map { prog ->
                Topic(
                    id = prog.categoryId,
                    name = "Categoría ${prog.categoryId.take(8)}", // Placeholder - should map to actual category name
                    progress = prog.score,
                    isRecent = true
                )
            }

            val recommendedTopicsList = listOf(
                Topic(id = "verbos", name = "Verbos simples", progress = 0),
                Topic(id = "preguntas", name = "Preguntas básicas", progress = 0)
            )

            _recentTopics.value = recentTopicsList.ifEmpty {
                listOf(
                    Topic(id = "abecedario", name = "Abecedario", progress = 0, isRecent = true)
                )
            }
            _recommendedTopics.value = recommendedTopicsList
        }
    }
    
    /**
     * Actualiza el progreso de un tema
     */
    fun updateTopicProgress(topicId: String, newProgress: Int) {
        viewModelScope.launch {
            // TODO: Implementar actualización en Firebase
        }
    }
    
    /**
     * Navega a la pantalla de un tema específico
     */
    fun navigateToTopic(topic: Topic) {
        // TODO: Implementar navegación
    }
    
    /**
     * Inicia el quiz diario
     */
    fun startDailyQuiz() {
        // TODO: Implementar lógica de quiz diario
    }
    
    /**
     * Inicia la práctica de reforzamiento
     */
    fun startPractice() {
        // TODO: Implementar lógica de práctica
    }
}
