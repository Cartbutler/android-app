package com.example.cartbutler.network

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("CartButlerPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_SESSION_ID = "session_id"
    }

    fun getSessionId(): String {
        return prefs.getString(KEY_SESSION_ID, null) ?: createNewSessionId()
    }

    private fun createNewSessionId(): String {
        val newId = UUID.randomUUID().toString()
        prefs.edit().putString(KEY_SESSION_ID, newId).apply()
        return newId
    }
}