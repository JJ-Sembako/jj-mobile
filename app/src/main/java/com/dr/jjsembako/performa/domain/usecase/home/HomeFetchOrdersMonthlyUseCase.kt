package com.dr.jjsembako.performa.domain.usecase.home

import com.dr.jjsembako.core.common.Resource
import kotlinx.coroutines.flow.Flow

interface HomeFetchOrdersMonthlyUseCase {

    suspend fun fetchOrders(
        minDate: String? = null,
        maxDate: String? = null,
        me: Int? = null,
    ): Flow<Resource<out Int?>>

}