package com.dr.jjsembako.performa.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.pesanan.domain.model.Order
import com.dr.jjsembako.performa.domain.model.Omzet
import com.dr.jjsembako.performa.domain.model.SelledProduct
import com.dr.jjsembako.performa.domain.usecase.home.HomeFetchOmzetUseCase
import com.dr.jjsembako.performa.domain.usecase.home.HomeFetchOrdersMonthlyUseCase
import com.dr.jjsembako.performa.domain.usecase.home.HomeFetchOrdersUseCase
import com.dr.jjsembako.performa.domain.usecase.home.HomeFetchSelledProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeFetchOmzetUseCase: HomeFetchOmzetUseCase,
    private val homeFetchSelledProductUseCase: HomeFetchSelledProductUseCase,
    private val homeFetchOrdersUseCase: HomeFetchOrdersUseCase,
    private val homeFetchOrdersMonthlyUseCase: HomeFetchOrdersMonthlyUseCase
) : ViewModel() {

    private val _stateFirst = MutableLiveData<StateResponse?>()
    val stateFirst: LiveData<StateResponse?> = _stateFirst

    private val _stateSecond = MutableLiveData<StateResponse?>()
    val stateSecond: LiveData<StateResponse?> = _stateSecond

    private val _stateThird = MutableLiveData<StateResponse?>()
    val stateThird: LiveData<StateResponse?> = _stateThird

    private val _stateFourth = MutableLiveData<StateResponse?>()
    val stateFourth: LiveData<StateResponse?> = _stateFourth

    private val _stateRefresh = MutableLiveData<StateResponse?>()
    val stateRefresh: LiveData<StateResponse?> = _stateRefresh

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    private val _statusCode = MutableLiveData<Int?>()
    val statusCode: LiveData<Int?> = _statusCode

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _dataOrders = MutableLiveData<List<Order?>?>()
    val dataOrders: LiveData<List<Order?>?> get() = _dataOrders

    private val _totalOrders = MutableLiveData<Int?>()
    val totalOrders: LiveData<Int?> get() = _totalOrders

    private val _dataOmzetMonthly = MutableLiveData<Omzet?>()
    val dataOmzetMonthly: LiveData<Omzet?> get() = _dataOmzetMonthly

    private val _dataSelledProductMonthly = MutableLiveData<List<SelledProduct?>?>()
    private val dataSelledProductMonthly: LiveData<List<SelledProduct?>?> get() = _dataSelledProductMonthly

    private val _totalSelledProductMonthly = MutableLiveData(0)
    val totalSelledProductMonthly: LiveData<Int> get() = _totalSelledProductMonthly

    private var _month: Int? = null
    private var _year: Int? = null
    private var _minDate: String? = null
    private var _maxDate: String? = null

    fun setTime(month: Int, year: Int, minDate: String, maxDate: String) {
        _month = month
        _year = year
        _minDate = minDate
        _maxDate = maxDate
        refresh()
    }

    fun refresh() {
        val month = _month ?: return
        val year = _year ?: return
        fetchOmzetMonthly(month + 1, year)
    }

    fun setStateFourth(state: StateResponse?) {
        _stateFourth.value = state
    }

    fun setStateRefresh(state: StateResponse?) {
        _stateRefresh.value = state
    }

    fun fetchOmzetMonthly(month: Int, year: Int) {
        viewModelScope.launch {
            homeFetchOmzetUseCase.fetchOmzetMonthly(month, year).collect {
                when (it) {
                    is Resource.Loading -> {
                        if (dataOmzetMonthly.value == null) {
                            _stateFirst.value = StateResponse.LOADING
                            _stateFourth.value = StateResponse.LOADING
                        } else _stateRefresh.value = StateResponse.LOADING
                    }

                    is Resource.Success -> {
                        if (dataOmzetMonthly.value == null) {
                            _stateFirst.value = StateResponse.SUCCESS
                        }
                        _message.value = it.message
                        _statusCode.value = it.status
                        _dataOmzetMonthly.value = it.data
                        fetchSelledProductMonthly(month, year)
                    }

                    is Resource.Error -> {
                        if (dataOmzetMonthly.value == null) {
                            _stateFirst.value = StateResponse.ERROR
                            _stateFourth.value = StateResponse.ERROR
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
            homeFetchSelledProductUseCase.fetchSelledProductMonthly(month, year).collect {
                when (it) {
                    is Resource.Loading -> {
                        if(dataSelledProductMonthly.value.isNullOrEmpty()) {
                            _stateSecond.value = StateResponse.LOADING
                        }
                    }

                    is Resource.Success -> {
                        if(dataSelledProductMonthly.value.isNullOrEmpty()) {
                            _stateSecond.value = StateResponse.SUCCESS
                        }
                        _message.value = it.message
                        _statusCode.value = it.status
                        _dataSelledProductMonthly.value = it.data
                        _totalSelledProductMonthly.value = it.data?.filterNotNull()
                            ?.sumOf { product -> product.numOfSelled } ?: 0
                        fetchOrders()
                    }

                    is Resource.Error -> {
                        if(dataSelledProductMonthly.value.isNullOrEmpty()) {
                            _stateSecond.value = StateResponse.ERROR
                            _stateFourth.value = StateResponse.ERROR
                        } else _stateRefresh.value = StateResponse.ERROR
                        _message.value = it.message
                        _statusCode.value = it.status
                    }
                }
            }
        }
    }

    private fun fetchOrders(
        limit: Int = 5,
        me: Int = 1,
    ) {
        viewModelScope.launch {
            homeFetchOrdersUseCase.fetchOrders(limit, me).collect {
                when (it) {
                    is Resource.Loading -> {
                        if(dataOrders.value.isNullOrEmpty()) {
                            _stateThird.value = StateResponse.LOADING
                        }
                    }

                    is Resource.Success -> {
                        if(dataOrders.value.isNullOrEmpty()) {
                            _stateThird.value = StateResponse.SUCCESS
                        }
                        _message.value = it.message
                        _statusCode.value = it.status
                        _dataOrders.value = it.data
                        fetchOrders(_minDate, _maxDate)
                    }

                    is Resource.Error -> {
                        if(dataOrders.value.isNullOrEmpty()) {
                            _stateThird.value = StateResponse.ERROR
                            _stateFourth.value = StateResponse.ERROR
                        } else _stateRefresh.value = StateResponse.ERROR
                        _message.value = it.message
                        _statusCode.value = it.status
                    }
                }
            }
        }
    }

    private fun fetchOrders(
        minDate: String? = null,
        maxDate: String? = null,
        me: Int = 1,
    ) {
        viewModelScope.launch {
            homeFetchOrdersMonthlyUseCase.fetchOrders(minDate, maxDate, me).collect {
                when (it) {
                    is Resource.Loading -> {
                        if(totalOrders.value == null) {
                            _stateFourth.value = StateResponse.LOADING
                        }
                    }

                    is Resource.Success -> {
                        if(totalOrders.value == null) {
                            _stateFourth.value = StateResponse.SUCCESS
                        } else _stateRefresh.value = StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
                        _totalOrders.value = it.data
                    }

                    is Resource.Error -> {
                        if(totalOrders.value == null) {
                            _stateFourth.value = StateResponse.ERROR
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