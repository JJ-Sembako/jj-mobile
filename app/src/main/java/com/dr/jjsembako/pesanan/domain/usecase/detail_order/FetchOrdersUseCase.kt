package com.dr.jjsembako.pesanan.domain.usecase.detail_order

import androidx.paging.PagingData
import com.dr.jjsembako.pesanan.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface FetchOrdersUseCase {

    suspend fun fetchOrders(
        search: String? = null,
        minDate: String? = null,
        maxDate: String? = null,
        me: Int? = null,
    ): Flow<PagingData<Order>>

}