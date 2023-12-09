package com.dr.jjsembako.feature_auth.presentation.password_recovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.feature_auth.domain.usecase.HandleUpdatePasswordFromRecoveryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PemulihanKataSandiViewModel @Inject constructor(private val handleUpdatePasswordFromRecoveryUseCase: HandleUpdatePasswordFromRecoveryUseCase) :
    ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _statusCode = MutableLiveData<Int?>()
    val statusCode: LiveData<Int?> = _statusCode

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun setState(state: StateResponse?) {
        _state.value = state
    }

    fun handleUpdatePasswordFromRecovery(
        username: String,
        newPassword: String,
        confNewPassword: String
    ) {
        viewModelScope.launch {
            handleUpdatePasswordFromRecoveryUseCase.handleUpdatePasswordFromRecovery(
                username,
                newPassword,
                confNewPassword
            ).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
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