package com.req.software.amoxcalli_app.ui.screens.library

import org.json.JSONArray
import org.json.JSONObject

/**
 * Parse an API-style JSON response into a list of LibraryWordUi.
 * Uses Android's org.json so no extra libs are required.
 */
fun parseLibraryJson(json: String): List<LibraryWordUi> {
    val result = mutableListOf<LibraryWordUi>()
    try {
        val root = JSONObject(json)
        val dataArray: JSONArray = root.optJSONArray("data") ?: JSONArray()
        for (i in 0 until dataArray.length()) {
            val item = dataArray.optJSONObject(i) ?: continue
            val id = item.optString("id", "")
            val name = item.optString("name", "")
            if (id.isNotBlank() && name.isNotBlank()) {
                result.add(LibraryWordUi(id = id, name = name, isFavorite = false))
            }
        }
    } catch (e: Exception) {
        // silent fail — return whatever parsed so far
    }
    return result
}

/*
fun parseCategoryJson(json: String): List<CategoryUi>{
    val result = mutableListOf<CategoryUi>()

}*/