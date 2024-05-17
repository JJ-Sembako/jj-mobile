package com.dr.jjsembako.akun.presentation.check_username

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.akun.domain.usecase.forgot.CheckAccountRecoveryActivationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PengecekanUsernameViewModel @Inject constructor(private val checkAccountRecoveryActivationUseCase: CheckAccountRecoveryActivationUseCase) :
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

    fun checkAccountRecoveryActivation(username: String) {
        viewModelScope.launch {
            checkAccountRecoveryActivationUseCase.checkAccountRecoveryActivation(username).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _message.value = it.message
                        _statusCode.value = it.status
                        val data = it.data
                        if (data != null && data.role == "SALES") {
                            if(data.isActive) _state.value = StateResponse.SUCCESS
                            else {
                                _state.value = StateResponse.ERROR
                                _message.value = "Akses ditolak!\n Pemulihan akun Anda belum aktif"
                            }
                        } else {
                            _state.value = StateResponse.ERROR
                            _message.value = "Akses ditolak!\n Aplikasi ini khusus untuk SALES"
                        }
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _message.value = it.message
                        _statusCode.value = it.status
                    }
                }
            }
        }
    }
}