package com.dr.jjsembako.feature_performance.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.performance.SelledData
import kotlinx.coroutines.flow.Flow

interface FetchSelledProductMonthlyUseCase {

    suspend fun fetchSelledProductMonthly(
        month: Int,
        year: Int
    ): Flow<Resource<out List<SelledData?>?>>

}