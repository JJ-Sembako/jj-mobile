package com.dr.jjsembako.performa.presentation.performance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.performa.domain.model.Omzet
import com.dr.jjsembako.performa.domain.model.SelledProduct
import com.dr.jjsembako.performa.domain.usecase.performance.FetchOmzetMonthlyUseCase
import com.dr.jjsembako.performa.domain.usecase.performance.FetchOmzetUseCase
import com.dr.jjsembako.performa.domain.usecase.performance.FetchSelledProductMonthlyUseCase
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

    private val _stateFirst = MutableLiveData<StateResponse?>()
    val stateFirst: LiveData<StateResponse?> = _stateFirst

    private val _stateSecond = MutableLiveData<StateResponse?>()
    val stateSecond: LiveData<StateResponse?> = _stateSecond

    private val _stateThird = MutableLiveData<StateResponse?>()
    val stateThird: LiveData<StateResponse?> = _stateThird

    private val _stateRefresh = MutableLiveData<StateResponse?>()
    val stateRefresh: LiveData<StateResponse?> = _stateRefresh

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    private val _statusCode = MutableLiveData<Int?>()
    val statusCode: LiveData<Int?> = _statusCode

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _dataOmzet = MutableLiveData<List<Omzet?>?>()
    val dataOmzet: LiveData<List<Omzet?>?> get() = _dataOmzet

    private val _dataOmzetOld = MutableLiveData<List<Omzet?>?>()
    private val dataOmzetOld: LiveData<List<Omzet?>?> get() = _dataOmzetOld

    private val _dataOmzetMonthly = MutableLiveData<Omzet?>()
    val dataOmzetMonthly: LiveData<Omzet?> get() = _dataOmzetMonthly

    private val _dataSelledProductMonthly = MutableLiveData<List<SelledProduct?>?>()
    val dataSelledProductMonthly: LiveData<List<SelledProduct?>?> get() = _dataSelledProductMonthly

    private val _totalSelledProductMonthly = MutableLiveData(0)
    val totalSelledProductMonthly: LiveData<Int> get() = _totalSelledProductMonthly

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
                        if (dataOmzetOld.value.isNullOrEmpty()) _stateFirst.value =
                            StateResponse.LOADING
                        else _stateRefresh.value = StateResponse.LOADING
                    }

                    is Resource.Success -> {
                        if (dataOmzetOld.value.isNullOrEmpty()) {
                            _stateFirst.value = StateResponse.SUCCESS
                            _dataOmzetOld.value = it.data?.reversed()
                            _dataOmzet.value = dataOmzetOld.value
                        }
                        _message.value = it.message
                        _statusCode.value = it.status
                        if(dataOmzetOld.value != it.data?.reversed()) {
                            _dataOmzetOld.value = it.data?.reversed()
                            _dataOmzet.value = dataOmzetOld.value
                        }
                        fetchOmzetMonthly(month, year)
                    }

                    is Resource.Error -> {
                        if (dataOmzetOld.value.isNullOrEmpty()) {
                            _stateFirst.value = StateResponse.ERROR
                            _stateThird.value = StateResponse.ERROR
                        } else _stateRefresh.value = StateResponse.ERROR
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
                        if (dataOmzetMonthly.value == null) {
                            _stateSecond.value = StateResponse.LOADING
                        }
                    }

                    is Resource.Success -> {
                        if (dataOmzetMonthly.value == null) {
                            _stateSecond.value = StateResponse.SUCCESS
                        }
                        _message.value = it.message
                        _statusCode.value = it.status
                        _dataOmzetMonthly.value = it.data
                        fetchSelledProductMonthly(month, year)
                    }

                    is Resource.Error -> {
                        if (dataOmzetMonthly.value == null) {
                            _stateSecond.value = StateResponse.ERROR
                        } else _stateRefresh.value = StateResponse.ERROR
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
                        if(dataSelledProductMonthly.value.isNullOrEmpty()) {
                            _stateThird.value = StateResponse.LOADING
                        }
                    }

                    is Resource.Success -> {
                        if(dataSelledProductMonthly.value.isNullOrEmpty()) {
                            _stateThird.value = StateResponse.SUCCESS
                        } else _stateRefresh.value = StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
                        _dataSelledProductMonthly.value = it.data
                        _totalSelledProductMonthly.value = it.data?.filterNotNull()
                            ?.sumOf { product -> product.numOfSelled } ?: 0
                    }

                    is Resource.Error -> {
                        if(dataSelledProductMonthly.value.isNullOrEmpty()) {
                            _stateThird.value = StateResponse.ERROR
                        } else _stateRefresh.value = StateResponse.ERROR
                        _message.value = it.message
                        _statusCode.value = it.status
                    }
                }
            }
            _isRefreshing.emit(false)
        }
    }

}