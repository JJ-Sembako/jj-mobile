package com.dr.jjsembako.feature_customer.presentation.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.feature_customer.domain.usecase.HandleCreateCustomerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TambahPelangganViewModel @Inject constructor(private val handleCreateCustomerUseCase: HandleCreateCustomerUseCase) :
    ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _statusCode = MutableLiveData<Int?>()
    val statusCode: LiveData<Int?> = _statusCode

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun setStateFirst(state: StateResponse?) {
        _state.value = state
    }

    fun handleCreateCustomer(
        name: String,
        shopName: String,
        address: String,
        gmapsLink: String,
        phoneNumber: String
    ) {
        viewModelScope.launch {
            handleCreateCustomerUseCase.handleCreateCustomer(
                name,
                shopName,
                address,
                gmapsLink,
                phoneNumber
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