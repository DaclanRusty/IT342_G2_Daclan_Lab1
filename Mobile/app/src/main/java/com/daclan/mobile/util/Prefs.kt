package com.daclan.mobile.util

import android.content.Context
import android.content.SharedPreferences

object Prefs {
    private const val PREFS_NAME = "daclan_prefs"
    private const val KEY_TOKEN = "jwt_token"
    private const val KEY_EMAIL = "user_email"
    private const val KEY_FIRST_NAME = "user_first_name"
    private const val KEY_LAST_NAME = "user_last_name"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveToken(context: Context, token: String) =
        prefs(context).edit().putString(KEY_TOKEN, token).apply()

    fun getToken(context: Context): String? =
        prefs(context).getString(KEY_TOKEN, null)

    fun getBearerToken(context: Context): String? {
        val token = getToken(context) ?: return null
        return "Bearer $token"
    }

    fun saveUser(context: Context, email: String, firstName: String, lastName: String) {
        prefs(context).edit()
            .putString(KEY_EMAIL, email)
            .putString(KEY_FIRST_NAME, firstName)
            .putString(KEY_LAST_NAME, lastName)
            .apply()
    }

    fun getEmail(context: Context): String? = prefs(context).getString(KEY_EMAIL, null)
    fun getFirstName(context: Context): String? = prefs(context).getString(KEY_FIRST_NAME, null)
    fun getLastName(context: Context): String? = prefs(context).getString(KEY_LAST_NAME, null)

    fun clear(context: Context) = prefs(context).edit().clear().apply()
}