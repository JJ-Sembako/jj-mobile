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

    private val _role = MutableStateFlow(getRole())
    val role: StateFlow<String> = _role

    private val _currentRole = MutableStateFlow(getCurrentRole())
    val currentRole: StateFlow<String> = _currentRole

    fun getToken(): String =
        sharedPreferences.getString(SharedPreferencesModule.TOKEN_KEY, "") ?: ""

    fun getUsername(): String =
        sharedPreferences.getString(SharedPreferencesModule.USERNAME_KEY, "") ?: ""

    fun getRole(): String =
        sharedPreferences.getString(SharedPreferencesModule.ROLE_KEY, "") ?: ""

    fun getCurrentRole(): String =
        sharedPreferences.getString(SharedPreferencesModule.CURRENT_ROLE_KEY, "") ?: ""

    fun setToken(token: String) {
        with(sharedPreferences.edit()) {
            putString(SharedPreferencesModule.TOKEN_KEY, token)
            apply()
        }
    }

    fun setUsername(username: String) {
        with(sharedPreferences.edit()) {
            putString(SharedPreferencesModule.USERNAME_KEY, username)
            apply()
        }
    }

    fun setRole(role: String) {
        with(sharedPreferences.edit()) {
            putString(SharedPreferencesModule.ROLE_KEY, role)
            apply()
        }
    }

    fun setCurrentRole(role: String) {
        with(sharedPreferences.edit()) {
            putString(SharedPreferencesModule.CURRENT_ROLE_KEY, role)
            apply()
        }
    }

    fun updateStateToken() {
        _token.value = getToken()
    }

    fun updateStateUsername() {
        _username.value = getUsername()
    }

    fun updateStateRole() {
        _role.value = getRole()
    }

    fun updateStateCurrentRole() {
        _currentRole.value = getCurrentRole()
    }
}