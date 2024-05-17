package com.dr.jjsembako.performa.domain.repository

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.performa.domain.model.Omzet
import com.dr.jjsembako.performa.domain.model.SelledProduct
import kotlinx.coroutines.flow.Flow

interface IPerformanceRepository {

    suspend fun fetchOmzet(): Flow<Resource<out List<Omzet?>?>>

    suspend fun fetchOmzetMonthly(month: Int, year: Int): Flow<Resource<out Omzet?>>

    suspend fun fetchSelledProductMonthly(
        month: Int,
        year: Int
    ): Flow<Resource<out List<SelledProduct?>?>>

}