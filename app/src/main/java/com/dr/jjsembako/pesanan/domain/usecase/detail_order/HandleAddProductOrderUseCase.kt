package com.dr.jjsembako.pesanan.domain.usecase.detail_order

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