package com.dr.jjsembako.feature_customer.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.feature_customer.domain.usecase.FetchDetailCustomerUseCase
import com.dr.jjsembako.feature_customer.domain.usecase.HandleDeleteCustomerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPelangganViewModel @Inject constructor(
    private val fetchDetailCustomerUseCase: FetchDetailCustomerUseCase,
    private val handleDeleteCustomerUseCase: HandleDeleteCustomerUseCase
) : ViewModel() {

    private val _stateFirst = MutableLiveData<StateResponse?>()
    val stateFirst: LiveData<StateResponse?> = _stateFirst

    private val _stateSecond = MutableLiveData<StateResponse?>()
    val stateSecond: LiveData<StateResponse?> = _stateSecond

    private val _stateRefresh = MutableLiveData<StateResponse?>()
    val stateRefresh: LiveData<StateResponse?> = _stateRefresh

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    private val _statusCode = MutableLiveData<Int?>()
    val statusCode: LiveData<Int?> = _statusCode

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _customerData = MutableLiveData<DataCustomer?>()
    val customerData: DataCustomer? get() = _customerData.value

    private var _id: String? = null
    fun setId(id: String) {
        _id = id
        init()
    }

    private fun init() {
        val id = _id ?: return
        fetchDetailCustomer(id)
    }

    fun setStateSecond(state: StateResponse?) {
        _stateSecond.value = state
    }

    fun setStateRefresh(state: StateResponse?) {
        _stateRefresh.value = state
    }

    fun refresh() {
        val id = _id ?: return
        fetchDetailCustomer(id)
    }

    fun fetchDetailCustomer(id: String) {
        viewModelScope.launch {
            fetchDetailCustomerUseCase.fetchDetailCustomer(id).collect {
                when (it) {
                    is Resource.Loading -> {
                        if (customerData == null) _stateFirst.value = StateResponse.LOADING
                        else _stateRefresh.value = StateResponse.LOADING
                    }
                    is Resource.Success -> {
                        if (customerData == null) _stateFirst.value = StateResponse.SUCCESS
                        else _stateRefresh.value =StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
                        _customerData.value = it.data
                    }

                    is Resource.Error -> {
                        if (customerData == null) _stateFirst.value = StateResponse.ERROR
                        else _stateRefresh.value = StateResponse.ERROR
                        _message.value = it.message
                        _statusCode.value = it.status
                    }
                }
            }
        }
    }

    fun handleDeleteCustomer(id: String) {
        viewModelScope.launch {
            handleDeleteCustomerUseCase.handleDeleteCustomer(id).collect {
                when (it) {
                    is Resource.Loading -> _stateSecond.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _stateSecond.value = StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
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
