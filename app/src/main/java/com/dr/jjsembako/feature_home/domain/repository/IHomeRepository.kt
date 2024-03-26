package com.dr.jjsembako.feature_home.domain.repository

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.OrderItem
import com.dr.jjsembako.core.data.remote.response.performance.OmzetData
import com.dr.jjsembako.core.data.remote.response.performance.SelledData
import kotlinx.coroutines.flow.Flow

interface IHomeRepository {

    suspend fun fetchOmzetMonthly(month: Int, year: Int): Flow<Resource<out OmzetData?>>

    suspend fun fetchSelledProductMonthly(
        month: Int,
        year: Int
    ): Flow<Resource<out List<SelledData?>?>>

    suspend fun fetchOrders(
        limit: Int? = null,
        me: Int? = null,
    ): Flow<Resource<out List<OrderItem?>?>>

    suspend fun fetchOrders(
        minDate: String? = null,
        maxDate: String? = null,
        me: Int? = null,
    ): Flow<Resource<out Int?>>

}