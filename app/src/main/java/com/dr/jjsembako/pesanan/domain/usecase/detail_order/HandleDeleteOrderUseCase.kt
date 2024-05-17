package com.dr.jjsembako.pesanan.domain.usecase.detail_order

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.DeleteHandleDeleteOrderResponse
import kotlinx.coroutines.flow.Flow

interface HandleDeleteOrderUseCase {

    suspend fun handleDeleteOrder(id: String): Flow<Resource<out DeleteHandleDeleteOrderResponse>>

}