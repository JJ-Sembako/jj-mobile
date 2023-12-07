package com.dr.jjsembako.core.utils

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.dr.jjsembako.core.di.SharedPreferencesModule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor(private val sharedPreferences: SharedPreferences) :
    ViewModel() {

    fun getToken(): String = sharedPreferences.getString("token", "") ?: ""

    fun setToken(token: String) {
        with(sharedPreferences.edit()) {
            putString(SharedPreferencesModule.PREFS_NAME, token)
            apply()
        }
    }

    fun getUsername(): String = sharedPreferences.getString(SharedPreferencesModule.USERNAME_KEY, "") ?: ""

    fun setUsername(username: String) {
        with(sharedPreferences.edit()) {
            putString(SharedPreferencesModule.USERNAME_KEY, username)
            apply()
        }
    }
}