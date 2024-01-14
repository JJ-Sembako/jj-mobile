package com.dr.jjsembako.feature_order.presentation.select_cust

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.feature_order.domain.usecase.FetchSelectCustUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PilihPelangganViewModel @Inject constructor(private val fetchSelectCustUseCase: FetchSelectCustUseCase) :
    ViewModel() {

    private val _customerState: MutableStateFlow<PagingData<DataCustomer>> =
        MutableStateFlow(value = PagingData.empty())
    val customerState: MutableStateFlow<PagingData<DataCustomer>> get() = _customerState

    fun fetchCustomers(searchQuery: String = "") {
        viewModelScope.launch {
            fetchSelectCustUseCase.fetchCustomers(searchQuery).collect {
                _customerState.value = it
            }
        }
    }

}