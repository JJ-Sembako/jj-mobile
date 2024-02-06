package com.dr.jjsembako.feature_history.domain.usecase.order

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.PostHandleAddProductOrderResponse
import kotlinx.coroutines.flow.Flow

interface HandleAddProductOrderUseCase {

    suspend fun handleAddProductOrder(
        id: String,
        productId: String,
        amountInUnit: Int,
        pricePerUnit: Long
    ): Flow<Resource<out PostHandleAddProductOrderResponse>>

}