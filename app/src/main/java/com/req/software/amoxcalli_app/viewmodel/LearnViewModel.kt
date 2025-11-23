package com.req.software.amoxcalli_app.viewmodel

// ===== IMPORTS DE LIFECYCLE =====
import android.util.Log.e
import androidx.lifecycle.ViewModel              // Clase base para ViewModels
import androidx.lifecycle.viewModelScope          // Scope de coroutines ligado al ciclo de vida del ViewModel

// ===== IMPORTS DE TU DOMINIO =====
import com.req.software.amoxcalli_app.domain.model.Topic
import com.req.software.amoxcalli_app.domain.model.UserStats

// ===== IMPORTS DE COROUTINES Y FLOW =====
import kotlinx.coroutines.flow.MutableStateFlow  // Flow mutable para emitir estados
import kotlinx.coroutines.flow.StateFlow         // Flow inmutable para observar estados
import kotlinx.coroutines.flow.asStateFlow       // Convierte MutableStateFlow a StateFlow
import kotlinx.coroutines.launch                 // Lanza coroutines en el viewModelScope
import kotlin.Boolean

data class LearnUiState(
    val userStats: UserStats = UserStats(
        coins = 0,
        energy = 100,
        streak = 0,
        experience = 0
    ),
    val topics: List<Topic> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class LearnViewModel : ViewModel() {

    // ===== ESTADO PRIVADO (Solo el ViewModel puede modificarlo) =====
    private val _uiState = MutableStateFlow(LearnUiState())

    // ===== ESTADO PÚBLICO (La UI solo puede LEER) =====
    val uiState: StateFlow<LearnUiState> = _uiState.asStateFlow()

    // ===== INICIALIZACIÓN =====
    init {
        // Se ejecuta cuando se crea el ViewModel
        loadTopics()
        loadUserStats()
    }

    // Aquí van los métodos privados y públicos...

    /**
     * Carga la lista de temas
     *
     * AHORA: Datos hardcodeados
     * FUTURO: Llamará a TopicRepository.getAllTopics()
     */
    private fun loadTopics() {
        viewModelScope.launch {
            // 📍 Paso 1: Mostrar loading
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // 📍 Paso 2: Operación pesada (BD, API, etc.)
                val topics = getMockTopics()

                // 📍 Paso 3: Actualizar UI con los datos
                _uiState.value = _uiState.value.copy(
                    topics = topics,
                    isLoading = false
                )
            } catch (e: Exception){
                // 📍 Paso 4: Manejar errores
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    /**
     * Datos temporales de prueba
     */
    private fun getMockTopics(): List<Topic> {
        return listOf(
            Topic(
                id = "abecedario",
                name = "Abecedario",
                progress = 75,
                isRecent = false
            ),
            Topic(
                id = "animales",
                name = "Animales",
                progress = 45,
                isRecent = false
            ),
            Topic(
                id = "vehiculos",
                name = "Vehículos",
                progress = 0,
                isRecent = false
            ),
            Topic(
                id = "verbos",
                name = "Verbos",
                progress = 100,
                isRecent = false
            ),
            Topic(
                id = "preguntas",
                name = "Preguntas",
                progress = 20,
                isRecent = false
            ),
            Topic(
                id = "familia",
                name = "Familia",
                progress = 60,
                isRecent = false
            ),
            Topic(
                id = "colores",
                name = "Colores",
                progress = 90,
                isRecent = false
            )
        )
    }

    /**
     * Carga las estadísticas del usuario
     *
     * FUTURO: Desde base de datos o API
     */
    private fun loadUserStats() {
        viewModelScope.launch {
            try {
                // Simulación de datos del usuario
                val stats = UserStats(
                    coins = 150,
                    energy = 85,
                    streak = 7,
                    experience = 450
                )

                // ✅ FALTABA ACTUALIZAR EL ESTADO
                _uiState.value = _uiState.value.copy(userStats = stats)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    /**
     * Maneja el click en un tema
     *
     * FUTURO: Navegará a la pantalla de lecciones del tema
     */
    fun onTopicClick(topic: Topic) {
        // Por ahora, solo mostramos un log
        println("📘 Tema seleccionado: ${topic.name} (${topic.progress}%)")

        // FUTURO: navegar a pantalla de lecciones
        // navController.navigate("lessons/${topic.id}")
    }
}



