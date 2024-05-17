package com.dr.jjsembako.pesanan.domain.usecase.create

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.model.OrderRequest
import com.dr.jjsembako.core.data.remote.response.order.DataAfterCreateOrder
import kotlinx.coroutines.flow.Flow

interface HandleCreateOrderUseCase {

    suspend fun handleCreateOrder(orderRequest: OrderRequest): Flow<Resource<out DataAfterCreateOrder?>>

}