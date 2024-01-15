package com.dr.jjsembako.feature_order.presentation.select_cust

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.feature_order.domain.usecase.FetchDetailSelectedCustUseCase
import com.dr.jjsembako.feature_order.domain.usecase.FetchSelectCustUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PilihPelangganViewModel @Inject constructor(
    private val fetchSelectCustUseCase: FetchSelectCustUseCase,
    private val fetchDetailSelectedCustUseCase: FetchDetailSelectedCustUseCase
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

    private val _selectedCustomer = MutableLiveData<DataCustomer?>()
    val selectedCustomer: LiveData<DataCustomer?> get() = _selectedCustomer

    private val _customerState: MutableStateFlow<PagingData<DataCustomer>> =
        MutableStateFlow(value = PagingData.empty())
    val customerState: MutableStateFlow<PagingData<DataCustomer>> get() = _customerState

    fun setStateRefresh(state: StateResponse?) {
        _stateRefresh.value = state
    }

    fun setSelectedCustomer(dataCustomer: DataCustomer?) {
        _selectedCustomer.value = dataCustomer
    }

    fun refresh(searchQuery: String = "") {
        fetchCustomers(searchQuery)
        if (selectedCustomer.value != null) fetchDetailCustomer(selectedCustomer.value!!.id)
    }

    fun fetchCustomers(searchQuery: String = "") {
        viewModelScope.launch {
            fetchSelectCustUseCase.fetchCustomers(searchQuery).collect {
                _customerState.value = it
            }
            _isRefreshing.emit(false)
        }
    }

    fun fetchDetailCustomer(id: String) {
        viewModelScope.launch {
            fetchDetailSelectedCustUseCase.fetchDetailCustomer(id).collect {
                when (it) {
                    is Resource.Loading -> {
                        if (selectedCustomer.value?.id.isNullOrEmpty()) _state.value =
                            StateResponse.LOADING
                        else _stateRefresh.value = StateResponse.LOADING
                    }

                    is Resource.Success -> {
                        if (selectedCustomer.value?.id.isNullOrEmpty()) _state.value =
                            StateResponse.SUCCESS
                        else _stateRefresh.value = StateResponse.SUCCESS
                        _message.value = it.message
                        _statusCode.value = it.status
                        _selectedCustomer.value = it.data
                    }

                    is Resource.Error -> {
                        if (selectedCustomer.value?.id.isNullOrEmpty()) _state.value =
                            StateResponse.ERROR
                        else _stateRefresh.value = StateResponse.ERROR
                        _message.value = it.message
                        _statusCode.value = it.status
                        if(statusCode.value!! in intArrayOf(400,401)){
                            _selectedCustomer.value = null
                        }
                    }
                }
            }
            _isRefreshing.emit(false)
        }
    }
}