package com.dr.jjsembako.feature_performance.domain.repository

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.performance.Omzet
import com.dr.jjsembako.core.data.remote.response.performance.SelledProduct
import kotlinx.coroutines.flow.Flow

interface IPerformanceRepository {

    suspend fun fetchOmzet(): Flow<Resource<out List<Omzet?>?>>

    suspend fun fetchOmzetMonthly(month: Int, year: Int): Flow<Resource<out Omzet?>>

    suspend fun fetchSelledProductMonthly(
        month: Int,
        year: Int
    ): Flow<Resource<out List<SelledProduct?>?>>

}