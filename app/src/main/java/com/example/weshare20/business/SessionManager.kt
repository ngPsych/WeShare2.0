package com.example.weshare20.business

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUserId(userId: Int) {
        sharedPreferences.edit().putInt("userId", userId).apply()
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt("userId", -1)
    }

    // Use clearSession when user logs out
    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }
}
