package com.dr.jjsembako.feature_home.domain.repository

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.Order
import com.dr.jjsembako.core.data.remote.response.performance.Omzet
import com.dr.jjsembako.core.data.remote.response.performance.SelledProduct
import kotlinx.coroutines.flow.Flow

interface IHomeRepository {

    suspend fun fetchOmzetMonthly(month: Int, year: Int): Flow<Resource<out Omzet?>>

    suspend fun fetchSelledProductMonthly(
        month: Int,
        year: Int
    ): Flow<Resource<out List<SelledProduct?>?>>

    suspend fun fetchOrders(
        limit: Int? = null,
        me: Int? = null,
    ): Flow<Resource<out List<Order?>?>>

    suspend fun fetchOrders(
        minDate: String? = null,
        maxDate: String? = null,
        me: Int? = null,
    ): Flow<Resource<out Int?>>

}