package com.dr.jjsembako.core.home.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.pesanan.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface HomeFetchOrdersUseCase {

    suspend fun fetchOrders(
        limit: Int? = null,
        me: Int? = null,
    ): Flow<Resource<out List<Order?>?>>

}