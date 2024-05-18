package com.dr.jjsembako.performa.domain.repository

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.pesanan.domain.model.Order
import com.dr.jjsembako.performa.domain.model.Omzet
import com.dr.jjsembako.performa.domain.model.SelledProduct
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