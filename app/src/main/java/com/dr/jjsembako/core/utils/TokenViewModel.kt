package com.dr.jjsembako.core.utils

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.dr.jjsembako.core.di.SharedPreferencesModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor(private val sharedPreferences: SharedPreferences) :
    ViewModel() {

    private val _token = MutableStateFlow(getToken())
    val token: StateFlow<String> = _token

    private val _username = MutableStateFlow(getUsername())
    val username: StateFlow<String> = _username

    fun getToken(): String =
        sharedPreferences.getString(SharedPreferencesModule.TOKEN_KEY, "") ?: ""

    fun setToken(token: String) {
        with(sharedPreferences.edit()) {
            putString(SharedPreferencesModule.TOKEN_KEY, token)
            apply()
        }
    }

    fun updateStateToken() {
        _token.value = getToken()
    }

    fun getUsername(): String =
        sharedPreferences.getString(SharedPreferencesModule.USERNAME_KEY, "") ?: ""

    fun setUsername(username: String) {
        with(sharedPreferences.edit()) {
            putString(SharedPreferencesModule.USERNAME_KEY, username)
            apply()
        }
    }

    fun updateStateUsername() {
        _username.value = getUsername()
    }
}