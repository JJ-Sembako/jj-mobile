package com.dr.jjsembako.feature_home.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.performance.OmzetData
import com.dr.jjsembako.feature_home.domain.repository.IHomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeFetchOmzetInteractor @Inject constructor(
    private val homeRepository: IHomeRepository
) : HomeFetchOmzetUseCase {
    override suspend fun fetchOmzetMonthly(month: Int, year: Int): Flow<Resource<out OmzetData?>> =
        homeRepository.fetchOmzetMonthly(month, year)
}