package com.dr.jjsembako.pesanan.domain.usecase.detail_order

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.pesanan.domain.model.DetailOrder
import kotlinx.coroutines.flow.Flow

interface FetchOrderUseCase {

    suspend fun fetchOrder(id: String): Flow<Resource<out DetailOrder?>>

}