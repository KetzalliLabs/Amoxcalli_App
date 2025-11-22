package com.req.software.amoxcalli_app.domain.model

/**
 * Modelo de dominio para representar un tema de aprendizaje
 */
data class Topic(
    val id: String,
    val name: String,
    val progress: Int, // 0-100
    val isRecent: Boolean = false
)

/**
 * Modelo para las estadísticas del usuario
 */
data class UserStats(
    val coins: Int = 0,
    val energy: Int = 0,
    val streak: Int = 0,
    val experience: Int = 0
)
