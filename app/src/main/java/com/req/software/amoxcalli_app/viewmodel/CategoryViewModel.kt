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
import android.util.Log

/**
 * ViewModel specifically for Category browsing screens
 * Separate from LibraryViewModel to avoid state conflicts
 */
class CategoryViewModel : ViewModel() {
    private val itemsService = RetrofitClient.itemsService
    private val authService = RetrofitClient.authService

    private val _categories = MutableStateFlow<List<CategoryDto>>(emptyList())
    val categories: StateFlow<List<CategoryDto>> = _categories.asStateFlow()

    private val _categorySignsMap = MutableStateFlow<Map<String, List<SignDto>>>(emptyMap())

    private val _currentCategoryId = MutableStateFlow<String?>(null)
    val currentCategoryId: StateFlow<String?> = _currentCategoryId.asStateFlow()

    private val _currentCategorySigns = MutableStateFlow<List<SignDto>>(emptyList())
    val currentCategorySigns: StateFlow<List<SignDto>> = _currentCategorySigns.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadCategories()
    }

    /**
     * Load all categories
     */
    fun loadCategories() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                Log.d("CategoryViewModel", "Loading categories...")
                val response = itemsService.getCategories()

                if (response.success) {
                    _categories.value = response.data
                    Log.d("CategoryViewModel", "Loaded ${response.data.size} categories")
                } else {
                    _error.value = "Failed to load categories"
                    Log.e("CategoryViewModel", "Failed to load categories")
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
                Log.e("CategoryViewModel", "Error loading categories: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Load signs for a specific category
     * This properly filters by category_id
     */
    fun loadSignsForCategory(categoryId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _currentCategoryId.value = categoryId

                Log.d("CategoryViewModel", "Loading signs for category: $categoryId")

                // Check cache first
                val cachedSigns = _categorySignsMap.value[categoryId]
                if (cachedSigns != null) {
                    Log.d("CategoryViewModel", "Using cached signs: ${cachedSigns.size} items")
                    _currentCategorySigns.value = cachedSigns
                    _isLoading.value = false
                    return@launch
                }

                // Fetch from API with category filter
                val response = itemsService.getSigns(
                    type = "signs",
                    categoryId = categoryId
                )

                if (response.success) {
                    val signs = response.data
                    Log.d("CategoryViewModel", "Loaded ${signs.size} signs for category $categoryId")

                    // Cache the results
                    _categorySignsMap.value = _categorySignsMap.value + (categoryId to signs)
                    _currentCategorySigns.value = signs

                    // Log first few signs for debugging
                    signs.take(3).forEach { sign ->
                        Log.d("CategoryViewModel", "  - Sign: ${sign.name} (category: ${sign.categoryId})")
                    }
                } else {
                    _error.value = "Failed to load signs"
                    Log.e("CategoryViewModel", "API returned success=false")
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
                Log.e("CategoryViewModel", "Error loading signs: ${e.message}", e)
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
                Log.d("CategoryViewModel", "Recorded view for sign: $signId")
            } catch (e: Exception) {
                Log.e("CategoryViewModel", "Error recording sign view: ${e.message}")
                // Silently fail - this is not critical
            }
        }
    }

    /**
     * Get category name by ID
     */
    fun getCategoryName(categoryId: String): String {
        return _categories.value.find { it.id == categoryId }?.name ?: "Categoría"
    }

    /**
     * Clear cache for a specific category
     */
    fun clearCategoryCache(categoryId: String) {
        _categorySignsMap.value = _categorySignsMap.value - categoryId
        Log.d("CategoryViewModel", "Cleared cache for category: $categoryId")
    }

    /**
     * Clear all caches
     */
    fun clearAllCaches() {
        _categorySignsMap.value = emptyMap()
        _currentCategorySigns.value = emptyList()
        _currentCategoryId.value = null
        Log.d("CategoryViewModel", "Cleared all caches")
    }
}
