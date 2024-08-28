package com.frcoding.reatailcashregister.data.prefs

import android.content.Context
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val context: Context
) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveUserId(userId: Int) {
        prefs.edit().putInt("current_user_id", userId).apply()
    }

    fun getUserId(): Int? {
        return prefs.getInt("current_user_id", -1).takeIf { it != -1 }
    }

    fun clearSession() {
        prefs.edit().remove("current_user_id").apply()
    }
}