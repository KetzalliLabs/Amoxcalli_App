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
     */
    fun getViewedSigns(): Set<String> {
        return prefs.getStringSet(VIEWED_SIGNS_KEY, emptySet()) ?: emptySet()
    }

    /**
     * Mark a sign as viewed
     */
    fun markAsViewed(signId: String) {
        val currentViewed = getViewedSigns().toMutableSet()
        currentViewed.add(signId)
        prefs.edit().putStringSet(VIEWED_SIGNS_KEY, currentViewed).apply()
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

