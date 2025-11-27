package com.req.software.amoxcalli_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.req.software.amoxcalli_app.data.dto.ExerciseDto
import com.req.software.amoxcalli_app.data.dto.ExerciseCompletionRequest
import com.req.software.amoxcalli_app.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Exercise/Quiz screens
 * Handles exercise fetching, answer validation, and completion recording
 * Game limits: 6 correct answers to win, 3 wrong answers to lose
 */
class ExerciseViewModel : ViewModel() {
    private val exercisesService = RetrofitClient.exercisesService
    private val authService = RetrofitClient.authService

    companion object {
        const val MAX_CORRECT_ANSWERS = 6
        const val MAX_WRONG_ANSWERS = 3
    }

    private val _exercises = MutableStateFlow<List<ExerciseDto>>(emptyList())
    val exercises: StateFlow<List<ExerciseDto>> = _exercises.asStateFlow()

    private val _currentExercise = MutableStateFlow<ExerciseDto?>(null)
    val currentExercise: StateFlow<ExerciseDto?> = _currentExercise.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectedAnswerId = MutableStateFlow<String?>(null)
    val selectedAnswerId: StateFlow<String?> = _selectedAnswerId.asStateFlow()

    private val _answerResult = MutableStateFlow<AnswerResult?>(null)
    val answerResult: StateFlow<AnswerResult?> = _answerResult.asStateFlow()

    private val _correctCount = MutableStateFlow(0)
    val correctCount: StateFlow<Int> = _correctCount.asStateFlow()

    private val _wrongCount = MutableStateFlow(0)
    val wrongCount: StateFlow<Int> = _wrongCount.asStateFlow()

    private val _sessionState = MutableStateFlow(SessionState.ACTIVE)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    private var startTime: Long = 0L
    private var sessionStartTime: Long = 0L
    private val completedExercises = mutableListOf<ExerciseCompletion>()
    private var sessionCategoryId: String? = null

    /**
     * Load exercises (optionally filtered by category)
     */
    fun loadExercises(categoryId: String? = null) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val response = exercisesService.getExercises(categoryId = categoryId)

                if (response.success && response.data.isNotEmpty()) {
                    _exercises.value = response.data
                    // Set first exercise as current
                    _currentExercise.value = response.data.firstOrNull()
                    startTime = System.currentTimeMillis()
                } else {
                    _error.value = "No exercises available"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Load a random exercise
     */
    fun loadRandomExercise() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _selectedAnswerId.value = null
                _answerResult.value = null

                // Initialize session if starting fresh
                if (sessionStartTime == 0L) {
                    sessionStartTime = System.currentTimeMillis()
                }

                val response = exercisesService.getExercises()

                if (response.success && response.data.isNotEmpty()) {
                    val randomExercise = response.data.random()
                    _currentExercise.value = randomExercise

                    // Track category for progress saving
                    if (sessionCategoryId == null) {
                        sessionCategoryId = randomExercise.categoryId
                    }

                    startTime = System.currentTimeMillis()
                } else {
                    _error.value = "No exercises available"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Select an answer
     */
    fun selectAnswer(answerId: String) {
        _selectedAnswerId.value = answerId
    }

    /**
     * Check if selected answer is correct
     */
    fun checkAnswer(authToken: String?) {
        val exercise = _currentExercise.value ?: return
        val selectedId = _selectedAnswerId.value ?: return

        // Don't allow checking if session is over
        if (_sessionState.value != SessionState.ACTIVE) return

        // Find the selected option
        val selectedOption = exercise.options.find { it.id == selectedId }

        if (selectedOption != null) {
            val isCorrect = selectedOption.isCorrect
            val timeTaken = System.currentTimeMillis() - startTime
            val score = if (isCorrect) 100 else 0

            // Update counts
            if (isCorrect) {
                _correctCount.value += 1
            } else {
                _wrongCount.value += 1
            }

            // Store exercise completion data
            completedExercises.add(
                ExerciseCompletion(
                    exerciseId = exercise.id,
                    isCorrect = isCorrect,
                    selectedSignId = selectedId,
                    timeTaken = timeTaken,
                    score = score
                )
            )

            _answerResult.value = AnswerResult(
                isCorrect = isCorrect,
                correctAnswerId = exercise.correctSignId,
                message = if (isCorrect) "¡Correcto! +10 XP, +5 Monedas" else "Incorrecto"
            )

            // Check if session should end
            checkSessionEnd(authToken)
        }
    }

    /**
     * Check if session should end based on correct/wrong counts
     */
    private fun checkSessionEnd(authToken: String?) {
        when {
            _correctCount.value >= MAX_CORRECT_ANSWERS -> {
                _sessionState.value = SessionState.WON
                saveSessionProgress(authToken)
            }
            _wrongCount.value >= MAX_WRONG_ANSWERS -> {
                _sessionState.value = SessionState.LOST
                saveSessionProgress(authToken)
            }
        }
    }

    /**
     * Save session progress to backend
     * Saves both per-question progress and category progress
     */
    private fun saveSessionProgress(authToken: String?) {
        if (authToken == null || completedExercises.isEmpty()) return

        viewModelScope.launch {
            try {
                // 1. Save each completed exercise (per-question progress)
                completedExercises.forEach { completion ->
                    try {
                        val request = ExerciseCompletionRequest(
                            isCorrect = completion.isCorrect,
                            selectedSignId = completion.selectedSignId,
                            timeTaken = completion.timeTaken,
                            score = completion.score
                        )
                        authService.recordExerciseCompletion(
                            authToken,
                            completion.exerciseId,
                            request
                        )
                    } catch (e: Exception) {
                        // Continue even if one fails
                    }
                }

                // 2. Save category progress
                sessionCategoryId?.let { categoryId ->
                    try {
                        saveCategoryProgress(authToken, categoryId)
                    } catch (e: Exception) {
                        // Continue even if category progress save fails
                    }
                }
            } catch (e: Exception) {
                // Session save failed but game is still over
            }
        }
    }

    /**
     * Save category progress based on session results
     */
    private suspend fun saveCategoryProgress(authToken: String, categoryId: String) {
        // Calculate session score (percentage of correct answers)
        val totalQuestions = completedExercises.size
        val correctAnswers = _correctCount.value
        val scorePercentage = if (totalQuestions > 0) {
            (correctAnswers * 100) / totalQuestions
        } else 0

        // Determine status based on session outcome
        val status = when (_sessionState.value) {
            SessionState.WON -> "completed"
            SessionState.LOST -> "in_progress"
            else -> "in_progress"
        }

        val progressRequest = com.req.software.amoxcalli_app.data.dto.CategoryProgressRequest(
            categoryId = categoryId,
            score = scorePercentage,
            status = status
        )

        authService.updateCategoryProgress(authToken, progressRequest)
    }

    /**
     * Move to next exercise (only if session is still active)
     */
    fun nextExercise() {
        // Don't advance if session is over
        if (_sessionState.value != SessionState.ACTIVE) return

        val current = _currentExercise.value
        val allExercises = _exercises.value

        if (allExercises.isNotEmpty() && current != null) {
            val currentIndex = allExercises.indexOf(current)
            val nextIndex = (currentIndex + 1) % allExercises.size
            _currentExercise.value = allExercises[nextIndex]
        } else {
            // If no exercises loaded, load random
            loadRandomExercise()
        }

        // Reset question state but keep session data
        _selectedAnswerId.value = null
        _answerResult.value = null
        startTime = System.currentTimeMillis()
    }

    /**
     * Reset exercise state and start new session
     */
    fun reset() {
        _selectedAnswerId.value = null
        _answerResult.value = null
        _currentExercise.value = null
        _exercises.value = emptyList()
        _error.value = null
        _correctCount.value = 0
        _wrongCount.value = 0
        _sessionState.value = SessionState.ACTIVE
        sessionStartTime = 0L
        sessionCategoryId = null
        completedExercises.clear()
    }

    /**
     * Get session summary
     */
    fun getSessionSummary(): SessionSummary {
        val totalTime = if (sessionStartTime > 0) {
            System.currentTimeMillis() - sessionStartTime
        } else 0L

        return SessionSummary(
            correctAnswers = _correctCount.value,
            wrongAnswers = _wrongCount.value,
            totalQuestions = completedExercises.size,
            totalTime = totalTime,
            state = _sessionState.value
        )
    }
}

/**
 * Session state
 */
enum class SessionState {
    ACTIVE,
    WON,
    LOST
}

/**
 * Exercise completion data
 */
data class ExerciseCompletion(
    val exerciseId: String,
    val isCorrect: Boolean,
    val selectedSignId: String,
    val timeTaken: Long,
    val score: Int
)

/**
 * Session summary
 */
data class SessionSummary(
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val totalQuestions: Int,
    val totalTime: Long,
    val state: SessionState
)

/**
 * Result of answer validation
 */
data class AnswerResult(
    val isCorrect: Boolean,
    val correctAnswerId: String,
    val message: String
)
