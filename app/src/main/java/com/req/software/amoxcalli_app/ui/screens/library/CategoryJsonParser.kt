// kotlin
package com.req.software.amoxcalli_app.ui.screens.library

import org.json.JSONArray
import org.json.JSONObject

/**
 * Parse an API-style categories JSON response into a list of Category.
 * Uses Android's org.json so no extra libs are required.
 */
fun parseCategoryJson(json: String): List<Category> {
    val result = mutableListOf<Category>()
    try {
        val root = JSONObject(json)
        val dataArray: JSONArray = root.optJSONArray("data") ?: JSONArray()
        for (i in 0 until dataArray.length()) {
            val item = dataArray.optJSONObject(i) ?: continue
            val id = item.optString("id", "")
            val name = item.optString("name", "")
            if (id.isNotBlank() && name.isNotBlank()) {
                result.add(Category(id = id, name = name))
            }
        }
    } catch (e: Exception) {
        // ignore and return whatever parsed so far
    }
    return result
}
