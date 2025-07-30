package com.frcoding.reatailcashregister.data.prefs

import android.content.Context
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val context: Context
) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveUserId(userId: Long) {
        prefs.edit().putLong("current_user_id", userId).apply()
    }

    fun getUserId(): Long? {
        return prefs.getLong("current_user_id", -1).takeIf { it != -1L }
    }

    fun clearSession() {
        prefs.edit().remove("current_user_id").apply()
    }
}