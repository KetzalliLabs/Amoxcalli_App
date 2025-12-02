package com.req.software.amoxcalli_app.config

/**
 * API Configuration for Amoxcalli Backend
 *
 * INSTRUCTIONS:
 * Replace BASE_URL with your actual backend API URL
 * Example: "https://your-api-domain.com/api/"
 * Make sure to include the trailing slash
 */
object ApiConfig {
    /**
     * TODO: Replace with your actual backend API URL
     * Example: "https://amoxcalli-api.example.com/api/"
     *
     * For Android Emulator: Use 10.0.2.2 (refers to host machine's localhost)
     * For Physical Device: Use your computer's local IP (e.g., 192.168.1.100)
     */
    const val BASE_URL = "https://ketzallidbapi-production.up.railway.app/api/"
    //const val BASE_URL = "http://10.0.2.2:3000/api/"


    /**
     * API Endpoints (relative to BASE_URL)
     */
    object Endpoints {
        const val LOGIN = "auth/login"
        const val REGISTER = "auth/register"
        const val USER_STATS = "auth/me/stats"
    }
}
