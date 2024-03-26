package com.dr.jjsembako.feature_customer.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.dr.jjsembako.core.data.remote.response.customer.Customer
import com.dr.jjsembako.feature_customer.domain.usecase.FetchCustomersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PelangganViewModel @Inject constructor(private val fetchCustomersUseCase: FetchCustomersUseCase) :
    ViewModel() {

    private val _customerState: MutableStateFlow<PagingData<Customer>> =
        MutableStateFlow(value = PagingData.empty())
    val customerState: MutableStateFlow<PagingData<Customer>> get() = _customerState

    fun fetchCustomers(searchQuery: String = "") {
        viewModelScope.launch {
            fetchCustomersUseCase.fetchCustomers(searchQuery).collect {
                _customerState.value = it
            }
        }
    }

}