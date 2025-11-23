package com.req.software.amoxcalli_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.req.software.amoxcalli_app.domain.model.Topic
import com.req.software.amoxcalli_app.domain.model.UserStats
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
    
    init {
        loadUserData()
    }
    
    /**
     * Carga los datos del usuario desde el repositorio
     * TODO: Implementar con datos reales de Firebase
     */
    private fun loadUserData() {
        viewModelScope.launch {
            // Datos de ejemplo - reemplazar con datos reales
            _userStats.value = UserStats(
                coins = 300,
                energy = 20,
                streak = 9,
                experience = 2500
            )
            
            _recentTopics.value = listOf(
                Topic(id = "abecedario", name = "Abecedario", progress = 100, isRecent = true),
                Topic(id = "animales", name = "Animales", progress = 20, isRecent = true),
                Topic(id = "vehiculos", name = "Vehículos", progress = 5, isRecent = true)
            )
            
            _recommendedTopics.value = listOf(
                Topic(id = "verbos", name = "Verbos simples", progress = 0),
                Topic(id = "preguntas", name = "Preguntas básicas", progress = 0)
            )
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
