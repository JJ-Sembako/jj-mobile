package com.dr.jjsembako.feature_history.domain.usecase.order

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.DetailOrder
import kotlinx.coroutines.flow.Flow

interface FetchOrderUseCase {

    suspend fun fetchOrder(id: String): Flow<Resource<out DetailOrder?>>

}