package com.req.software.amoxcalli_app.service

import com.req.software.amoxcalli_app.data.dto.SignDto
import com.req.software.amoxcalli_app.data.dto.CategoryDto
import com.req.software.amoxcalli_app.data.dto.ListResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Items API Service
 * Handles signs and categories
 */
interface ItemsService {
    /**
     * Get all signs
     * @param type "signs"
     * @param categoryId Optional category filter
     * @return List of signs
     */
    @GET("items")
    suspend fun getSigns(
        @Query("type") type: String = "signs",
        @Query("category_id") categoryId: String? = null
    ): ListResponse<SignDto>

    /**
     * Get all categories
     * @param type "categories"
     * @return List of categories
     */
    @GET("items")
    suspend fun getCategories(
        @Query("type") type: String = "categories"
    ): ListResponse<CategoryDto>
}
