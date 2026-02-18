package com.daclan.mobile.util

import android.content.Context

object SessionManager {

    private const val PREFS_NAME = "daclan_prefs"
    private const val KEY_TOKEN = "jwt_token"
    private const val KEY_EMAIL = "user_email"
    private const val KEY_FIRST_NAME = "user_first_name"
    private const val KEY_LAST_NAME = "user_last_name"

    fun saveSession(
        context: Context,
        token: String,
        email: String,
        firstName: String,
        lastName: String
    ) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_EMAIL, email)
            .putString(KEY_FIRST_NAME, firstName)
            .putString(KEY_LAST_NAME, lastName)
            .apply()
    }

    fun getToken(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TOKEN, null)

    fun getBearerToken(context: Context): String? {
        val token = getToken(context) ?: return null
        return "Bearer $token"
    }

    fun getFirstName(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_FIRST_NAME, null)

    fun isLoggedIn(context: Context): Boolean = getToken(context) != null

    fun clearSession(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().clear().apply()
    }
}