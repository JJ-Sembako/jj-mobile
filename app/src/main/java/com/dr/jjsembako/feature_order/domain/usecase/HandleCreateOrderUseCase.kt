package com.dr.jjsembako.feature_order.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.model.OrderProduct
import com.dr.jjsembako.core.data.remote.response.order.PostHandleCreateOrderResponse
import kotlinx.coroutines.flow.Flow

interface HandleCreateOrderUseCase {

    suspend fun handleCreateOrder(
        customerId: String,
        products: List<OrderProduct>,
        paymentStatus: String
    ): Flow<Resource<out PostHandleCreateOrderResponse?>>

}