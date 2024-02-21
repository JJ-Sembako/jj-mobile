package com.dr.jjsembako.feature_performance.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.performance.OmzetData
import kotlinx.coroutines.flow.Flow

interface FetchOmzetMonthlyUseCase {

    suspend fun fetchOmzetMonthly(month: Int, year: Int): Flow<Resource<out OmzetData?>>

}