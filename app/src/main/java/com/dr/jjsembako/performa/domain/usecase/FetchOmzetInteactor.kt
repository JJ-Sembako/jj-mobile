package com.dr.jjsembako.performa.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.performa.domain.model.Omzet
import com.dr.jjsembako.performa.domain.repository.IPerformanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchOmzetInteactor @Inject constructor(
    private val performanceRepository: IPerformanceRepository
) : FetchOmzetUseCase {
    override suspend fun fetchOmzet(): Flow<Resource<out List<Omzet?>?>> =
        performanceRepository.fetchOmzet()
}