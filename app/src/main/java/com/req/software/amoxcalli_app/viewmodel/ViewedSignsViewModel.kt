package com.req.software.amoxcalli_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.req.software.amoxcalli_app.data.local.ViewedSignsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel for managing viewed signs locally (frontend only)
 */
class ViewedSignsViewModel(application: Application) : AndroidViewModel(application) {
    private val viewedSignsManager = ViewedSignsManager(application)

    private val _viewedSignIds = MutableStateFlow<Set<String>>(emptySet())
    val viewedSignIds: StateFlow<Set<String>> = _viewedSignIds.asStateFlow()

    init {
        loadViewedSigns()
    }

    /**
     * Load viewed signs from local storage
     */
    fun loadViewedSigns() {
        _viewedSignIds.value = viewedSignsManager.getViewedSigns()
    }

    /**
     * Mark a sign as viewed
     */
    fun markSignAsViewed(signId: String) {
        viewedSignsManager.markAsViewed(signId)
        _viewedSignIds.value = viewedSignsManager.getViewedSigns()
    }

    /**
     * Check if a sign is viewed
     */
    fun isSignViewed(signId: String): Boolean {
        return _viewedSignIds.value.contains(signId)
    }

    /**
     * Get total viewed count
     */
    fun getViewedCount(): Int {
        return _viewedSignIds.value.size
    }

    /**
     * Get viewed count for specific signs
     */
    fun getViewedCountForSigns(signIds: List<String>): Int {
        return signIds.count { _viewedSignIds.value.contains(it) }
    }

    /**
     * Clear all viewed signs
     */
    fun clearAll() {
        viewedSignsManager.clearAll()
        _viewedSignIds.value = emptySet()
    }
}

