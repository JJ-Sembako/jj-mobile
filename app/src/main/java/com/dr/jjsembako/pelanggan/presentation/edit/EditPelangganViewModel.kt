package com.dr.jjsembako.pelanggan.presentation.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.pelanggan.domain.model.Customer
import com.dr.jjsembako.pelanggan.domain.usecase.FetchDetailCustomerUseCase
import com.dr.jjsembako.pelanggan.domain.usecase.HandleUpdateCustomerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPelangganViewModel @Inject constructor(
    private val fetchDetailCustomerUseCase: FetchDetailCustomerUseCase,
    private val handleUpdateCustomerUseCase: HandleUpdateCustomerUseCase
) : ViewModel() {

    private val _stateFirst = MutableLiveData<StateResponse?>()
    val stateFirst: LiveData<StateResponse?> = _stateFirst

    private val _stateSecond = MutableLiveData<StateResponse?>()
    val stateSecond: LiveData<StateResponse?> = _stateSecond

    private val _statusCode = MutableLiveData<Int?>()
    val statusCode: LiveData<Int?> = _statusCode

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _customerData = MutableLiveData<Customer?>()
    val customerData: Customer? get() = _customerData.value

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

    fun fetchDetailCustomer(id: String) {
        viewModelScope.launch {
            fetchDetailCustomerUseCase.fetchDetailCustomer(id).collect {
                when (it) {
                    is Resource.Loading -> _stateFirst.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _stateFirst.value = StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
                        _customerData.value = it.data
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

    fun handleUpdateCustomer(
        id: String,
        name: String,
        shopName: String,
        address: String,
        gmapsLink: String,
        phoneNumber: String
    ) {
        viewModelScope.launch {
            handleUpdateCustomerUseCase.handleUpdateCustomer(
                id,
                name,
                shopName,
                address,
                gmapsLink,
                phoneNumber
            ).collect {
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