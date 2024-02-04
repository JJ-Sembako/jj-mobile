package com.dr.jjsembako.feature_history.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.dr.jjsembako.core.data.remote.response.order.OrderDataItem
import com.dr.jjsembako.feature_history.domain.usecase.FetchOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RiwayatViewModel @Inject constructor(private val fetchOrdersUseCase: FetchOrdersUseCase) :
    ViewModel() {

    init {
        fetchOrders(search = null, minDate = null, maxDate = null)
    }

    private val _orderState: MutableStateFlow<PagingData<OrderDataItem>> =
        MutableStateFlow(value = PagingData.empty())
    val orderState: MutableStateFlow<PagingData<OrderDataItem>> get() = _orderState

    fun fetchOrders(
        search: String?,
        minDate: String?,
        maxDate: String?,
        me: Int = 1
    ) {
        viewModelScope.launch {
            fetchOrdersUseCase.fetchOrders(
                search = search,
                minDate = minDate,
                maxDate = maxDate,
                me = me
            ).collect { _orderState.value = it }
        }
    }

}