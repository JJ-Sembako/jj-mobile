package com.dr.jjsembako.feature_history.domain.usecase.order

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.PatchHandleUpdateProductOrderResponse
import kotlinx.coroutines.flow.Flow

interface HandleUpdateProductOrderUseCase {

    suspend fun handleUpdateProductOrder(
        id: String,
        productId: String,
        amountInUnit: Int,
        pricePerUnit: Long
    ): Flow<Resource<out PatchHandleUpdateProductOrderResponse>>

}