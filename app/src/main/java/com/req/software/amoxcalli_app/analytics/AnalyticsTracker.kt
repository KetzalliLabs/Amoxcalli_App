package com.req.software.amoxcalli_app.analytics

import android.util.Log

/**
 * Analytics abstraction for tracking user events
 * MVP: Logs to console, can be replaced with Firebase Analytics or other SDK
 */
interface AnalyticsTracker {
    fun trackScreenView(screenName: String, params: Map<String, Any> = emptyMap())
    fun trackInteraction(eventName: String, params: Map<String, Any> = emptyMap())
    fun trackNavigation(destination: String, params: Map<String, Any> = emptyMap())
}

/**
 * Default implementation that logs events
 */
class LogAnalyticsTracker : AnalyticsTracker {
    override fun trackScreenView(screenName: String, params: Map<String, Any>) {
        Log.d("Analytics", "Screen View: $screenName, params: $params")
    }

    override fun trackInteraction(eventName: String, params: Map<String, Any>) {
        Log.d("Analytics", "Interaction: $eventName, params: $params")
    }

    override fun trackNavigation(destination: String, params: Map<String, Any>) {
        Log.d("Analytics", "Navigation: $destination, params: $params")
    }
}
