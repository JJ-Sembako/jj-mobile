package com.dr.jjsembako.feature_customer.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.feature_customer.domain.usecase.FetchCustomersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PelangganViewModel @Inject constructor(private val fetchCustomersUseCase: FetchCustomersUseCase) :
    ViewModel() {

    init {
        fetchCustomers()
    }

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _statusCode = MutableLiveData<Int?>()
    val statusCode: LiveData<Int?> = _statusCode

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _customerList = MutableLiveData<List<DataCustomer?>?>()
    val customerList: LiveData<List<DataCustomer?>?> = _customerList

    fun setState(state: StateResponse?) {
        _state.value = state
    }

    fun fetchCustomers(
        search: String? = null,
        page: Int? = null,
        limit: Int? = null
    ) {
        viewModelScope.launch {
            fetchCustomersUseCase.fetchCustomers(search, page, limit).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
                        _customerList.value = it.data
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