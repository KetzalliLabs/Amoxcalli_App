package com.req.software.amoxcalli_app.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

/**
 * Local XP (Experience Points) manager using SharedPreferences
 * Handles XP tracking without backend dependency
 * Mirrors the StreakManager pattern for consistency
 */
class XPManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    companion object {
        private const val PREFS_NAME = "xp_prefs"
        private const val KEY_TOTAL_XP = "total_xp"
        private const val KEY_SESSION_XP = "session_xp"

        // XP rewards
        const val XP_PER_CORRECT_ANSWER = 10
        const val XP_PER_EXERCISE_COMPLETION = 50
        const val XP_DAILY_LOGIN_BONUS = 25
    }

    /**
     * Get current total XP
     */
    fun getTotalXP(): Int {
        return prefs.getInt(KEY_TOTAL_XP, 0)
    }

    /**
     * Get XP earned in current session
     */
    fun getSessionXP(): Int {
        return prefs.getInt(KEY_SESSION_XP, 0)
    }

    /**
     * Add XP and return the new total
     * @param amount XP amount to add
     * @return New total XP
     */
    fun addXP(amount: Int): Int {
        val currentTotal = getTotalXP()
        val newTotal = currentTotal + amount

        val currentSession = getSessionXP()
        val newSession = currentSession + amount

        prefs.edit()
            .putInt(KEY_TOTAL_XP, newTotal)
            .putInt(KEY_SESSION_XP, newSession)
            .apply()

        Log.d("XPManager", "Added $amount XP. Total: $newTotal (Session: $newSession)")
        return newTotal
    }

    /**
     * Award XP for a correct answer
     * @return Amount of XP awarded
     */
    fun awardCorrectAnswer(): Int {
        addXP(XP_PER_CORRECT_ANSWER)
        return XP_PER_CORRECT_ANSWER
    }

    /**
     * Award XP for completing an exercise
     * @return Amount of XP awarded
     */
    fun awardExerciseCompletion(): Int {
        addXP(XP_PER_EXERCISE_COMPLETION)
        return XP_PER_EXERCISE_COMPLETION
    }

    /**
     * Award daily login bonus
     * @return Amount of XP awarded
     */
    fun awardDailyBonus(): Int {
        addXP(XP_DAILY_LOGIN_BONUS)
        return XP_DAILY_LOGIN_BONUS
    }

    /**
     * Reset session XP (call when starting new session)
     */
    fun resetSessionXP() {
        prefs.edit()
            .putInt(KEY_SESSION_XP, 0)
            .apply()
        Log.d("XPManager", "Session XP reset")
    }

    /**
     * Set total XP (for testing or sync purposes)
     */
    fun setTotalXP(amount: Int) {
        prefs.edit()
            .putInt(KEY_TOTAL_XP, amount)
            .apply()
        Log.d("XPManager", "Total XP set to $amount")
    }

    /**
     * Reset all XP data (for testing)
     */
    fun resetAllXP() {
        prefs.edit()
            .remove(KEY_TOTAL_XP)
            .remove(KEY_SESSION_XP)
            .apply()
        Log.d("XPManager", "All XP data reset")
    }

    /**
     * Get XP data as a readable object
     */
    data class XPData(
        val totalXP: Int,
        val sessionXP: Int
    )

    fun getXPData(): XPData {
        return XPData(
            totalXP = getTotalXP(),
            sessionXP = getSessionXP()
        )
    }
}
