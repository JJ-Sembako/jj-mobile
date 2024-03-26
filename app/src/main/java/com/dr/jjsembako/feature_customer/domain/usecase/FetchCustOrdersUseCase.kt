package com.dr.jjsembako.feature_customer.domain.usecase

import androidx.paging.PagingData
import com.dr.jjsembako.core.data.remote.response.order.OrderItem
import kotlinx.coroutines.flow.Flow

interface FetchCustOrdersUseCase {

    suspend fun fetchOrders(
        search: String? = null,
        minDate: String? = null,
        maxDate: String? = null,
        me: Int? = null,
        customerId: String
    ): Flow<PagingData<OrderItem>>

}