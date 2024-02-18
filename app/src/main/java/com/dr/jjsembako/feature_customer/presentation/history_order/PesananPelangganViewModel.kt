package com.dr.jjsembako.feature_customer.presentation.history_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.dr.jjsembako.core.data.remote.response.order.OrderDataItem
import com.dr.jjsembako.feature_customer.domain.usecase.FetchCustOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PesananPelangganViewModel @Inject constructor(
    private val fetchCustOrdersUseCase: FetchCustOrdersUseCase
) : ViewModel() {

    private val _orderState: MutableStateFlow<PagingData<OrderDataItem>> =
        MutableStateFlow(value = PagingData.empty())
    val orderState: MutableStateFlow<PagingData<OrderDataItem>> get() = _orderState

    private val _id = MutableLiveData<String?>()
    private val id: LiveData<String?> = _id
    fun setId(id: String) {
        _id.value = id
        fetchOrders(null, null, null)
    }

    fun fetchOrders(
        search: String?,
        minDate: String?,
        maxDate: String?
    ) {
        if (id.value != null) {
            viewModelScope.launch {
                fetchCustOrdersUseCase.fetchOrders(
                    search = search, minDate = minDate, maxDate = maxDate,
                    me = 0, customerId = id.value!!
                ).collect { _orderState.value = it }
            }
        }
    }
}