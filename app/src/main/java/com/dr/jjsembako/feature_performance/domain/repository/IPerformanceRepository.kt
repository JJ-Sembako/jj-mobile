package com.dr.jjsembako.feature_performance.domain.repository

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.performance.OmzetData
import com.dr.jjsembako.core.data.remote.response.performance.SelledData
import kotlinx.coroutines.flow.Flow

interface IPerformanceRepository {

    suspend fun fetchOmzet(): Flow<Resource<out List<OmzetData?>?>>

    suspend fun fetchOmzetMonthly(month: Int, year: Int): Flow<Resource<out OmzetData?>>

    suspend fun fetchSelledProductMonthly(
        month: Int,
        year: Int
    ): Flow<Resource<out List<SelledData?>?>>

}