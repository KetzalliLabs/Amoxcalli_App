package com.req.software.amoxcalli_app.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/**
 * Local streak (racha) manager using SharedPreferences
 * Handles daily login tracking without backend dependency
 */
class StreakManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    companion object {
        private const val PREFS_NAME = "streak_prefs"
        private const val KEY_CURRENT_STREAK = "current_streak"
        private const val KEY_BEST_STREAK = "best_streak"
        private const val KEY_LAST_CHECK_DATE = "last_check_date"

        private const val DATE_FORMAT = "yyyy-MM-dd"
    }

    /**
     * Check and update streak for today
     * Call this when user opens the app
     * @return Updated current streak count
     */
    fun checkAndUpdateStreak(): Int {
        val today = getTodayDate()
        val lastCheckDate = prefs.getString(KEY_LAST_CHECK_DATE, null)
        val currentStreak = prefs.getInt(KEY_CURRENT_STREAK, 0)

        Log.d("StreakManager", "Today: $today, Last check: $lastCheckDate, Current streak: $currentStreak")

        // If already checked today, return current streak
        if (lastCheckDate == today) {
            Log.d("StreakManager", "Already checked today, returning streak: $currentStreak")
            return currentStreak
        }

        val newStreak = when {
            // First time ever
            lastCheckDate == null -> {
                Log.d("StreakManager", "First time tracking streak")
                1
            }
            // Consecutive day (yesterday)
            isYesterday(lastCheckDate, today) -> {
                Log.d("StreakManager", "Consecutive day! Incrementing streak")
                currentStreak + 1
            }
            // Streak broken (more than 1 day gap)
            else -> {
                Log.d("StreakManager", "Streak broken! Resetting to 1")
                1
            }
        }

        // Update best streak if current is higher
        val bestStreak = prefs.getInt(KEY_BEST_STREAK, 0)
        if (newStreak > bestStreak) {
            prefs.edit().putInt(KEY_BEST_STREAK, newStreak).apply()
            Log.d("StreakManager", "New best streak: $newStreak")
        }

        // Save new streak and today's date
        prefs.edit()
            .putInt(KEY_CURRENT_STREAK, newStreak)
            .putString(KEY_LAST_CHECK_DATE, today)
            .apply()

        Log.d("StreakManager", "Streak updated to: $newStreak")
        return newStreak
    }

    /**
     * Get current streak without updating
     */
    fun getCurrentStreak(): Int {
        return prefs.getInt(KEY_CURRENT_STREAK, 0)
    }

    /**
     * Get best/longest streak ever achieved
     */
    fun getBestStreak(): Int {
        return prefs.getInt(KEY_BEST_STREAK, 0)
    }

    /**
     * Get last check date
     */
    fun getLastCheckDate(): String? {
        return prefs.getString(KEY_LAST_CHECK_DATE, null)
    }

    /**
     * Reset all streak data (for testing or user reset)
     */
    fun resetStreak() {
        prefs.edit()
            .remove(KEY_CURRENT_STREAK)
            .remove(KEY_BEST_STREAK)
            .remove(KEY_LAST_CHECK_DATE)
            .apply()
        Log.d("StreakManager", "Streak data reset")
    }

    /**
     * Manually set streak (for testing purposes)
     */
    fun setStreakForTesting(streak: Int) {
        prefs.edit()
            .putInt(KEY_CURRENT_STREAK, streak)
            .putString(KEY_LAST_CHECK_DATE, getTodayDate())
            .apply()
        Log.d("StreakManager", "Streak set to $streak for testing")
    }

    // Helper functions

    private fun getTodayDate(): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.US)
        return dateFormat.format(Date())
    }

    private fun isYesterday(lastDate: String, todayDate: String): Boolean {
        try {
            val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.US)
            val last = dateFormat.parse(lastDate) ?: return false
            val today = dateFormat.parse(todayDate) ?: return false

            val calendar = Calendar.getInstance()
            calendar.time = today
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val yesterday = calendar.time

            // Compare dates (ignoring time)
            val yesterdayStr = dateFormat.format(yesterday)
            return lastDate == yesterdayStr
        } catch (e: Exception) {
            Log.e("StreakManager", "Error comparing dates", e)
            return false
        }
    }

    /**
     * Get streak data as a readable object
     */
    data class StreakData(
        val currentStreak: Int,
        val bestStreak: Int,
        val lastCheckDate: String?
    )

    fun getStreakData(): StreakData {
        return StreakData(
            currentStreak = getCurrentStreak(),
            bestStreak = getBestStreak(),
            lastCheckDate = getLastCheckDate()
        )
    }
}
