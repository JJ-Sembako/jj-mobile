package com.dr.jjsembako.feature_history.domain.repository

import androidx.paging.PagingData
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.DeleteHandleDeleteOrderResponse
import com.dr.jjsembako.core.data.remote.response.order.DeleteHandleDeleteProductOrderResponse
import com.dr.jjsembako.core.data.remote.response.order.DetailOrderData
import com.dr.jjsembako.core.data.remote.response.order.OrderDataItem
import kotlinx.coroutines.flow.Flow

interface IHistoryRepository {

    suspend fun fetchOrders(
        search: String? = null,
        minDate: String? = null,
        maxDate: String? = null,
        me: Int? = null,
    ): Flow<PagingData<OrderDataItem>>

    suspend fun fetchOrder(id: String): Flow<Resource<out DetailOrderData?>>

    suspend fun handleDeleteProductOrder(
        id: String,
        productId: String
    ): Flow<Resource<out DeleteHandleDeleteProductOrderResponse>>

    suspend fun handleDeleteOrder(id: String): Flow<Resource<out DeleteHandleDeleteOrderResponse>>

}