package com.dr.jjsembako.feature_auth.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.feature_auth.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _statusCode = MutableLiveData<Int?>()
    val statusCode: LiveData<Int?> = _statusCode

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> = _token

    private val _username = MutableLiveData<String?>()
    val username: LiveData<String?> = _username

    fun setState(state: StateResponse?) {
        _state.value = state
    }

    fun handleLogin(username: String, password: String) {
        viewModelScope.launch {
            loginUseCase.handleLogin(username, password).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
                        val data = it.data
                        if (data != null && data.role == "SALES") {
                            _token.value = data.token
                            _username.value = data.username
                        } else {
                            _state.value = StateResponse.ERROR
                            _message.value = "Akses ditolak!\n Aplikasi ini khusus untuk SALES."
                        }
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _message.value = it.message
                        _statusCode.value = it.status
                    }

                    else -> {}
                }
            }
        }
    }
}