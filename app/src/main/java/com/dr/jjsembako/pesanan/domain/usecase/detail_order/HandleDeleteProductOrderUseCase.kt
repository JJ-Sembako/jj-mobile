package com.dr.jjsembako.pesanan.domain.usecase.detail_order

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.DeleteHandleDeleteProductOrderResponse
import kotlinx.coroutines.flow.Flow

interface HandleDeleteProductOrderUseCase {

    suspend fun handleDeleteProductOrder(
        id: String,
        productId: String
    ): Flow<Resource<out DeleteHandleDeleteProductOrderResponse>>

}