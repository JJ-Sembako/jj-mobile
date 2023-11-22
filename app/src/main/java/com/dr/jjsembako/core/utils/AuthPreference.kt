package com.dr.jjsembako.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

object AuthPreference {
    private const val PREFS_NAME = "auth_pref"
    private const val TOKEN = "token"
    private lateinit var preference: SharedPreferences

    fun initialize(context: Context) {
        preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    @SuppressLint("CommitPrefEdits")
    fun setToken(token: String) {
        val editor = preference.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun clearToken() {
        val editor = preference.edit()
        editor.remove(TOKEN)
        editor.apply()
    }

    fun getToken(): String? = preference.getString(TOKEN, "")
}