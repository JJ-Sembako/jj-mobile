package com.dr.jjsembako.feature_performance.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.performance.Omzet
import com.dr.jjsembako.feature_performance.domain.repository.IPerformanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchOmzetInteactor @Inject constructor(
    private val performanceRepository: IPerformanceRepository
) : FetchOmzetUseCase {
    override suspend fun fetchOmzet(): Flow<Resource<out List<Omzet?>?>> =
        performanceRepository.fetchOmzet()
}