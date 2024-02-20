package com.dr.jjsembako.feature_performance.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.performance.SelledData
import com.dr.jjsembako.feature_performance.domain.repository.IPerformanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchSelledProductMonthlyInteractor @Inject constructor(
    private val performanceRepository: IPerformanceRepository
) : FetchSelledProductMonthlyUseCase {
    override suspend fun fetchSelledProductMonthly(
        month: Int,
        year: Int
    ): Flow<Resource<out List<SelledData?>?>> =
        performanceRepository.fetchSelledProductMonthly(month, year)
}