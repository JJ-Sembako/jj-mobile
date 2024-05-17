package com.dr.jjsembako.pelanggan.domain.usecase

import androidx.paging.PagingData
import com.dr.jjsembako.pesanan.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface FetchCustOrdersUseCase {

    suspend fun fetchOrders(
        search: String? = null,
        minDate: String? = null,
        maxDate: String? = null,
        me: Int? = null,
        customerId: String
    ): Flow<PagingData<Order>>

}