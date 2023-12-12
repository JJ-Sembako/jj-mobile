package com.dr.jjsembako.feature_customer.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.feature_customer.domain.usecase.FetchCustomersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PelangganViewModel @Inject constructor(private val fetchCustomersUseCase: FetchCustomersUseCase) :
    ViewModel() {

    private val _customerState: MutableStateFlow<PagingData<DataCustomer>> = MutableStateFlow(value = PagingData.empty())
    val customerState: MutableStateFlow<PagingData<DataCustomer>> get() = _customerState

    fun fetchCustomers(searchQuery: String = "") {
        viewModelScope.launch {
            fetchCustomersUseCase.fetchCustomers(searchQuery).collect {
                _customerState.value = it
            }
        }
    }

}