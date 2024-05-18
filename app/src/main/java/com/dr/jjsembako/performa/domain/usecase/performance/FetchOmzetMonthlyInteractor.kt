package com.dr.jjsembako.performa.domain.usecase.performance

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.performa.domain.model.Omzet
import com.dr.jjsembako.performa.domain.repository.IPerformanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchOmzetMonthlyInteractor @Inject constructor(
    private val performanceRepository: IPerformanceRepository
) : FetchOmzetMonthlyUseCase {
    override suspend fun fetchOmzetMonthly(month: Int, year: Int): Flow<Resource<out Omzet?>> =
        performanceRepository.fetchOmzetMonthly(month, year)
}