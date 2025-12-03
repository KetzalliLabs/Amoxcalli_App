package com.req.software.amoxcalli_app.data.local

import android.content.Context
import android.content.SharedPreferences

/**
 * Local manager for tracking viewed signs without backend
 */
class ViewedSignsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "viewed_signs_prefs",
        Context.MODE_PRIVATE
    )

    private val VIEWED_SIGNS_KEY = "viewed_signs_set"

    /**
     * Get all viewed sign IDs
     * IMPORTANT: Returns a new HashSet to avoid SharedPreferences reference issues
     */
    fun getViewedSigns(): Set<String> {
        val storedSet = prefs.getStringSet(VIEWED_SIGNS_KEY, null)
        return if (storedSet != null) {
            HashSet(storedSet)
        } else {
            emptySet()
        }
    }

    /**
     * Mark a sign as viewed
     * Creates a completely new HashSet to ensure SharedPreferences detects the change
     */
    fun markAsViewed(signId: String) {
        val currentViewed = HashSet(getViewedSigns())
        currentViewed.add(signId)
        // Use commit() instead of apply() to ensure synchronous write
        prefs.edit().putStringSet(VIEWED_SIGNS_KEY, currentViewed).commit()
    }

    /**
     * Check if a sign has been viewed
     */
    fun isViewed(signId: String): Boolean {
        return getViewedSigns().contains(signId)
    }

    /**
     * Get count of viewed signs
     */
    fun getViewedCount(): Int {
        return getViewedSigns().size
    }

    /**
     * Get viewed signs for a specific category
     */
    fun getViewedCountForCategory(categoryId: String, allSignsInCategory: List<String>): Int {
        val viewedSigns = getViewedSigns()
        return allSignsInCategory.count { viewedSigns.contains(it) }
    }

    /**
     * Clear all viewed signs (for testing/reset)
     */
    fun clearAll() {
        prefs.edit().remove(VIEWED_SIGNS_KEY).apply()
    }
}

