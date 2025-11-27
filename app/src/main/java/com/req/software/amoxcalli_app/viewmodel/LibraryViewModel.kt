package com.req.software.amoxcalli_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.req.software.amoxcalli_app.data.dto.SignDto
import com.req.software.amoxcalli_app.data.dto.CategoryDto
import com.req.software.amoxcalli_app.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Library/Dictionary screens
 * Handles signs and categories from backend
 */
class LibraryViewModel : ViewModel() {
    private val itemsService = RetrofitClient.itemsService
    private val authService = RetrofitClient.authService

    private val _signs = MutableStateFlow<List<SignDto>>(emptyList())
    val signs: StateFlow<List<SignDto>> = _signs.asStateFlow()

    private val _categories = MutableStateFlow<List<CategoryDto>>(emptyList())
    val categories: StateFlow<List<CategoryDto>> = _categories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    init {
        loadCategories()
        loadSigns()
    }

    /**
     * Load all categories
     */
    fun loadCategories() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val response = itemsService.getCategories()

                if (response.success) {
                    _categories.value = response.data
                } else {
                    _error.value = "Failed to load categories"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Load signs (optionally filtered by category)
     */
    fun loadSigns(categoryId: String? = null) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _selectedCategory.value = categoryId

                val response = itemsService.getSigns(categoryId = categoryId)

                if (response.success) {
                    _signs.value = response.data
                } else {
                    _error.value = "Failed to load signs"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Record a sign view
     */
    fun recordSignView(signId: String, authToken: String) {
        viewModelScope.launch {
            try {
                authService.recordSignView(authToken, signId)
            } catch (e: Exception) {
                // Silently fail - this is not critical
            }
        }
    }

    /**
     * Search signs by name
     */
    fun searchSigns(query: String) {
        if (query.isBlank()) {
            loadSigns(_selectedCategory.value)
            return
        }

        val filtered = _signs.value.filter {
            it.name.contains(query, ignoreCase = true)
        }
        _signs.value = filtered
    }

    /**
     * Clear filters and reload all signs
     */
    fun clearFilters() {
        _selectedCategory.value = null
        loadSigns()
    }
}
