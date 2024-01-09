package com.dr.jjsembako.feature_auth.presentation.recovery_question

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.feature_auth.domain.usecase.CheckAccountRecoveryAnswerUseCase
import com.dr.jjsembako.feature_auth.domain.usecase.FetchAccountRecoveryQuestionByUsernameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PertanyaanPemulihanViewModel @Inject constructor(
    private val fetchAccountRecoveryQuestionByUsernameUseCase: FetchAccountRecoveryQuestionByUsernameUseCase,
    private val checkAccountRecoveryAnswerUseCase: CheckAccountRecoveryAnswerUseCase
) : ViewModel() {

    private val _stateFirst = MutableLiveData<StateResponse?>()
    val stateFirst: LiveData<StateResponse?> = _stateFirst

    private val _stateSecond = MutableLiveData<StateResponse?>()
    val stateSecond: LiveData<StateResponse?> = _stateSecond

    private val _statusCode = MutableLiveData<Int?>()
    val statusCode: LiveData<Int?> = _statusCode

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _question = MutableLiveData<String?>()
    val question: String? get() = _question.value

    private var _username: String? = null
    fun setUsername(username: String) {
        _username = username
        init()
    }

    private fun init() {
        val username = _username ?: return
        fetchAccountRecoveryQuestionByUsername(username)
    }

    fun setStateSecond(state: StateResponse?) {
        _stateSecond.value = state
    }

    fun fetchAccountRecoveryQuestionByUsername(username: String) {
        viewModelScope.launch {
            fetchAccountRecoveryQuestionByUsernameUseCase.fetchAccountRecoveryQuestionByUsername(
                username
            ).collect {
                when (it) {
                    is Resource.Loading -> _stateFirst.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _message.value = it.message
                        _statusCode.value = it.status
                        _stateFirst.value = StateResponse.SUCCESS
                        _question.value = it.data?.question
                    }

                    is Resource.Error -> {
                        _stateFirst.value = StateResponse.ERROR
                        _message.value = it.message
                        _statusCode.value = it.status
                    }
                }
            }
        }
    }

    fun checkAccountRecoveryAnswer(username: String, answer: String) {
        viewModelScope.launch {
            checkAccountRecoveryAnswerUseCase.checkAccountRecoveryAnswer(username, answer).collect {
                when (it) {
                    is Resource.Loading -> _stateSecond.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _message.value = it.message
                        _statusCode.value = it.status
                        if (it.data?.isValid == true) _stateSecond.value = StateResponse.SUCCESS
                        else {
                            _stateSecond.value = StateResponse.ERROR
                            _message.value = "Jawaban tidak sesuai!"
                        }
                    }

                    is Resource.Error -> {
                        _stateSecond.value = StateResponse.ERROR
                        _message.value = it.message
                        _statusCode.value = it.status
                    }
                }
            }
        }
    }
}