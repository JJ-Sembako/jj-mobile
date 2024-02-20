package com.dr.jjsembako.feature_performance.presentation.performance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.remote.response.performance.OmzetData
import com.dr.jjsembako.core.data.remote.response.performance.SelledData
import com.dr.jjsembako.feature_performance.domain.usecase.FetchOmzetMonthlyUseCase
import com.dr.jjsembako.feature_performance.domain.usecase.FetchOmzetUseCase
import com.dr.jjsembako.feature_performance.domain.usecase.FetchSelledProductMonthlyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerformaViewModel @Inject constructor(
    private val fetchOmzetUseCase: FetchOmzetUseCase,
    private val fetchOmzetMonthlyUseCase: FetchOmzetMonthlyUseCase,
    private val fetchSelledProductMonthlyUseCase: FetchSelledProductMonthlyUseCase
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _stateRefresh = MutableLiveData<StateResponse?>()
    val stateRefresh: LiveData<StateResponse?> = _stateRefresh

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    private val _statusCode = MutableLiveData<Int?>()
    val statusCode: LiveData<Int?> = _statusCode

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _dataOmzet = MutableLiveData<List<OmzetData?>?>()
    val dataOmzet: LiveData<List<OmzetData?>?> get() = _dataOmzet

    private val _dataOmzetMonthly = MutableLiveData<OmzetData?>()
    val dataOmzetMonthly: LiveData<OmzetData?> get() = _dataOmzetMonthly

    private val _dataSelledProductMonthly = MutableLiveData<List<SelledData?>?>()
    val dataSelledProductMonthly: LiveData<List<SelledData?>?> get() = _dataSelledProductMonthly

    private var _month: Int? = null
    private var _year: Int? = null

    fun setTime(month: Int, year: Int) {
        _month = month
        _year = year
        refresh()
    }

    fun refresh() {
        val month = _month ?: return
        val year = _year ?: return
        fetchOmzet(month + 1, year)
    }

    fun setStateRefresh(state: StateResponse?) {
        _stateRefresh.value = state
    }

    private fun fetchOmzet(month: Int, year: Int) {
        viewModelScope.launch {
            fetchOmzetUseCase.fetchOmzet().collect {
                when (it) {
                    is Resource.Loading -> {
                        if (dataOmzet.value.isNullOrEmpty()) _state.value =
                            StateResponse.LOADING
                        else _stateRefresh.value = StateResponse.LOADING
                    }

                    is Resource.Success -> {
                        if (dataOmzet.value.isNullOrEmpty()) _state.value =
                            StateResponse.LOADING
                        else _stateRefresh.value = StateResponse.LOADING
                        _message.value = it.message
                        _statusCode.value = it.status
                        _dataOmzet.value = it.data
                        fetchOmzetMonthly(month, year)
                    }

                    is Resource.Error -> {
                        if (dataOmzet.value.isNullOrEmpty()) _state.value =
                            StateResponse.ERROR
                        else _stateRefresh.value = StateResponse.ERROR
                        _message.value = it.message
                        _statusCode.value = it.status
                    }
                }
            }
        }
    }

    private fun fetchOmzetMonthly(month: Int, year: Int) {
        viewModelScope.launch {
            fetchOmzetMonthlyUseCase.fetchOmzetMonthly(month, year).collect {
                when (it) {
                    is Resource.Loading -> {
                        if (dataOmzet.value.isNullOrEmpty()) _state.value =
                            StateResponse.LOADING
                        else _stateRefresh.value = StateResponse.LOADING
                    }

                    is Resource.Success -> {
                        if (dataOmzet.value.isNullOrEmpty()) _state.value =
                            StateResponse.LOADING
                        else _stateRefresh.value = StateResponse.LOADING
                        _message.value = it.message
                        _statusCode.value = it.status
                        _dataOmzetMonthly.value = it.data
                        fetchSelledProductMonthly(month, year)
                    }

                    is Resource.Error -> {
                        if (dataOmzet.value.isNullOrEmpty()) _state.value =
                            StateResponse.ERROR
                        else _stateRefresh.value = StateResponse.ERROR
                        _message.value = it.message
                        _statusCode.value = it.status
                    }
                }
            }
        }
    }

    private fun fetchSelledProductMonthly(month: Int, year: Int) {
        viewModelScope.launch {
            fetchSelledProductMonthlyUseCase.fetchSelledProductMonthly(month, year).collect {
                when (it) {
                    is Resource.Loading -> {
                        if (dataOmzet.value.isNullOrEmpty()) _state.value =
                            StateResponse.LOADING
                        else _stateRefresh.value = StateResponse.LOADING
                    }

                    is Resource.Success -> {
                        if (dataOmzet.value.isNullOrEmpty()) _state.value =
                            StateResponse.SUCCESS
                        else _stateRefresh.value = StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
                        _dataSelledProductMonthly.value = it.data
                    }

                    is Resource.Error -> {
                        if (dataOmzet.value.isNullOrEmpty()) _state.value =
                            StateResponse.ERROR
                        else _stateRefresh.value = StateResponse.ERROR
                        _message.value = it.message
                        _statusCode.value = it.status
                    }
                }
            }
            _isRefreshing.emit(false)
        }
    }

}