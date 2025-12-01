package com.req.software.amoxcalli_app.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Local repository for managing favorites using SharedPreferences
 * No backend synchronization - all data stored locally
 */
class FavoritesRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "favorites_prefs"
        private const val KEY_FAVORITES = "favorites"
    }

    /**
     * Get all favorite signs
     */
    fun getFavorites(): List<LocalFavoriteSign> {
        val json = prefs.getString(KEY_FAVORITES, null) ?: return emptyList()
        val type = object : TypeToken<List<LocalFavoriteSign>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Get all favorite IDs
     */
    fun getFavoriteIds(): Set<String> {
        return getFavorites().map { it.id }.toSet()
    }

    /**
     * Check if a sign is favorited
     */
    fun isFavorite(signId: String): Boolean {
        return getFavoriteIds().contains(signId)
    }

    /**
     * Add a sign to favorites
     */
    fun addToFavorites(sign: LocalFavoriteSign): Boolean {
        val currentFavorites = getFavorites().toMutableList()

        // Don't add if already exists
        if (currentFavorites.any { it.id == sign.id }) {
            return false
        }

        currentFavorites.add(sign)
        saveFavorites(currentFavorites)
        return true
    }

    /**
     * Remove a sign from favorites
     */
    fun removeFromFavorites(signId: String): Boolean {
        val currentFavorites = getFavorites().toMutableList()
        val removed = currentFavorites.removeAll { it.id == signId }

        if (removed) {
            saveFavorites(currentFavorites)
        }

        return removed
    }

    /**
     * Toggle favorite status
     */
    fun toggleFavorite(sign: LocalFavoriteSign): Boolean {
        return if (isFavorite(sign.id)) {
            removeFromFavorites(sign.id)
            false
        } else {
            addToFavorites(sign)
            true
        }
    }

    /**
     * Clear all favorites
     */
    fun clearAllFavorites() {
        prefs.edit().remove(KEY_FAVORITES).apply()
    }

    /**
     * Save favorites list to SharedPreferences
     */
    private fun saveFavorites(favorites: List<LocalFavoriteSign>) {
        val json = gson.toJson(favorites)
        prefs.edit().putString(KEY_FAVORITES, json).apply()
    }
}
