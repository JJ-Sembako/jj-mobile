package com.dr.jjsembako.akun.presentation.recovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.remote.response.account.DataRecoveryQuestion
import com.dr.jjsembako.akun.domain.usecase.recovery.ActivateAccountRecoveryUseCase
import com.dr.jjsembako.akun.domain.usecase.recovery.DeactivateAccountRecoveryUseCase
import com.dr.jjsembako.akun.domain.usecase.recovery.GetAllRecoveryQuestionUseCase
import com.dr.jjsembako.akun.domain.usecase.recovery.GetDataAccountRecoveryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PemulihanAkunViewModel @Inject constructor(
    private val getAllRecoveryQuestionUseCase: GetAllRecoveryQuestionUseCase,
    private val getDataAccountRecoveryUseCase: GetDataAccountRecoveryUseCase,
    private val activateAccountRecoveryUseCase: ActivateAccountRecoveryUseCase,
    private val deactivateAccountRecoveryUseCase: DeactivateAccountRecoveryUseCase
) : ViewModel() {

    private val _stateFirst = MutableLiveData<StateResponse?>()
    val stateFirst: LiveData<StateResponse?> = _stateFirst

    private val _stateSecond = MutableLiveData<StateResponse?>()
    val stateSecond: LiveData<StateResponse?> = _stateSecond

    private val _stateThird = MutableLiveData<StateResponse?>()
    val stateThird: LiveData<StateResponse?> = _stateThird

    private val _statusCode = MutableLiveData<Int?>()
    val statusCode: LiveData<Int?> = _statusCode

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _questionList = MutableLiveData<List<DataRecoveryQuestion?>?>()
    val questionList: LiveData<List<DataRecoveryQuestion?>?> = _questionList

    private val _isActive = MutableLiveData<Boolean?>()
    val isActive: Boolean? get() = _isActive.value

    private val _answer = MutableLiveData<String?>()
    val answer: String? get() = _answer.value

    private val _idQuestion = MutableLiveData<String?>()
    val idQuestion: String? get() = _idQuestion.value

    init {
        fetchAccountRecoveryQuestions()
    }

    fun setStateThird(state: StateResponse?) {
        _stateThird.value = state
    }

    fun fetchAccountRecoveryQuestions() {
        viewModelScope.launch {
            getAllRecoveryQuestionUseCase.fetchAccountRecoveryQuestions().collect {
                when (it) {
                    is Resource.Loading -> _stateFirst.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _stateFirst.value = StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
                        _questionList.value = it.data
                        fetchAccountRecovery()
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

    fun fetchAccountRecovery() {
        viewModelScope.launch {
            getDataAccountRecoveryUseCase.fetchAccountRecovery().collect {
                when (it) {
                    is Resource.Loading -> _stateSecond.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _stateSecond.value = StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
                        _isActive.value = it.data?.isActive
                        _answer.value = it.data?.answer
                        _idQuestion.value = it.data?.idQuestion
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

    fun handleActivateAccountRecovery(idQuestion: String, answer: String) {
        viewModelScope.launch {
            activateAccountRecoveryUseCase.handleActivateAccountRecovery(idQuestion, answer)
                .collect {
                    when (it) {
                        is Resource.Loading -> _stateThird.value = StateResponse.LOADING
                        is Resource.Success -> {
                            _stateThird.value = StateResponse.SUCCESS
                            _message.value = it.message
                            _statusCode.value = it.status
                        }

                        is Resource.Error -> {
                            _stateThird.value = StateResponse.ERROR
                            _message.value = it.message
                            _statusCode.value = it.status
                        }
                    }
                }
        }
    }

    fun handleDeactivateAccountRecovery() {
        viewModelScope.launch {
            deactivateAccountRecoveryUseCase.handleDeactivateAccountRecovery().collect {
                when (it) {
                    is Resource.Loading -> _stateThird.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _stateThird.value = StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
                    }

                    is Resource.Error -> {
                        _stateThird.value = StateResponse.ERROR
                        _message.value = it.message
                        _statusCode.value = it.status
                    }
                }
            }
        }
    }

}