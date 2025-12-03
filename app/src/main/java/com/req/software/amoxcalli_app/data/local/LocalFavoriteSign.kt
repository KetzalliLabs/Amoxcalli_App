package com.req.software.amoxcalli_app.data.local

/**
 * Local data model for favorite signs
 * Stored locally without backend synchronization
 */
data class LocalFavoriteSign(
    val id: String,
    val name: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
