package com.dr.jjsembako.feature_history.domain.repository

import androidx.paging.PagingData
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.DetailOrderData
import com.dr.jjsembako.core.data.remote.response.order.OrderDataItem
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface IHistoryRepository {

    suspend fun fetchOrders(
        @Query("search") search: String? = null,
        @Query("minDate") minDate: String? = null,
        @Query("maxDate") maxDate: String? = null,
        @Query("me") me: Int? = null,
    ): Flow<PagingData<OrderDataItem>>

    suspend fun fetchOrder(id: String): Flow<Resource<out DetailOrderData?>>

}