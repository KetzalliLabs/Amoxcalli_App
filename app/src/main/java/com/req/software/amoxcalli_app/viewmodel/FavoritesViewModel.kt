package com.req.software.amoxcalli_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.req.software.amoxcalli_app.data.local.FavoritesRepository
import com.req.software.amoxcalli_app.data.local.LocalFavoriteSign
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing user favorites - Local storage only
 * No backend synchronization to avoid 401 errors and network issues
 */
class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FavoritesRepository(application)

    private val _favorites = MutableStateFlow<List<LocalFavoriteSign>>(emptyList())
    val favorites: StateFlow<List<LocalFavoriteSign>> = _favorites.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Load favorites from local storage on initialization
        loadFavorites()
    }

    /**
     * Load user's favorite signs from local storage
     */
    fun loadFavorites() {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val localFavorites = repository.getFavorites()
                _favorites.value = localFavorites
                _favoriteIds.value = localFavorites.map { it.id }.toSet()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Add a sign to favorites (local storage only)
     */
    fun addToFavorites(
        signId: String,
        name: String,
        description: String? = null,
        imageUrl: String? = null,
        videoUrl: String? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val sign = LocalFavoriteSign(
                id = signId,
                name = name,
                description = description,
                imageUrl = imageUrl,
                videoUrl = videoUrl
            )

            val added = repository.addToFavorites(sign)
            if (added) {
                _favoriteIds.value = _favoriteIds.value + signId
                _favorites.value = repository.getFavorites()
                onSuccess()
            }
        }
    }

    /**
     * Remove a sign from favorites (local storage only)
     */
    fun removeFromFavorites(signId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val removed = repository.removeFromFavorites(signId)
            if (removed) {
                _favoriteIds.value = _favoriteIds.value - signId
                _favorites.value = _favorites.value.filter { it.id != signId }
                onSuccess()
            }
        }
    }

    /**
     * Toggle favorite status (local storage only)
     * @param signId The sign ID
     * @param name The sign name (required for adding)
     * @param description Optional description
     * @param imageUrl Optional image URL
     * @param videoUrl Optional video URL
     */
    fun toggleFavorite(
        signId: String,
        name: String = "",
        description: String? = null,
        imageUrl: String? = null,
        videoUrl: String? = null
    ) {
        if (_favoriteIds.value.contains(signId)) {
            removeFromFavorites(signId)
        } else {
            addToFavorites(signId, name, description, imageUrl, videoUrl)
        }
    }

    /**
     * Check if a sign is favorited
     */
    fun isFavorite(signId: String): Boolean {
        return _favoriteIds.value.contains(signId)
    }

    /**
     * Clear all favorites
     */
    fun clearAllFavorites() {
        viewModelScope.launch {
            repository.clearAllFavorites()
            _favorites.value = emptyList()
            _favoriteIds.value = emptySet()
        }
    }
}
