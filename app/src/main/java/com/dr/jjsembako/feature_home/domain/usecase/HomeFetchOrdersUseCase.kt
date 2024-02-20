package com.dr.jjsembako.feature_home.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.OrderDataItem
import kotlinx.coroutines.flow.Flow

interface HomeFetchOrdersUseCase {

    suspend fun fetchOrders(
        limit: Int? = null,
        me: Int? = null,
    ): Flow<Resource<out List<OrderDataItem?>?>>

}