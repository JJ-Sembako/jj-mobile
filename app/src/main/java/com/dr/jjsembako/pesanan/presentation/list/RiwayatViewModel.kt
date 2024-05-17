package com.dr.jjsembako.pesanan.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.dr.jjsembako.pesanan.domain.model.Order
import com.dr.jjsembako.pesanan.domain.usecase.detail_order.FetchOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RiwayatViewModel @Inject constructor(private val fetchOrdersUseCase: FetchOrdersUseCase) :
    ViewModel() {

    private val _orderState: MutableStateFlow<PagingData<Order>> =
        MutableStateFlow(value = PagingData.empty())
    val orderState: MutableStateFlow<PagingData<Order>> get() = _orderState

    init {
        fetchOrders(search = null, minDate = null, maxDate = null)
    }

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