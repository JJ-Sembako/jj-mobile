package com.dr.jjsembako.feature_history.domain.usecase.order

import androidx.paging.PagingData
import com.dr.jjsembako.core.data.remote.response.order.OrderDataItem
import kotlinx.coroutines.flow.Flow

interface FetchOrdersUseCase {

    suspend fun fetchOrders(
        search: String? = null,
        minDate: String? = null,
        maxDate: String? = null,
        me: Int? = null,
    ): Flow<PagingData<OrderDataItem>>

}