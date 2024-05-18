package com.dr.jjsembako.performa.domain.usecase.home

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.performa.domain.model.Omzet
import com.dr.jjsembako.performa.domain.repository.IHomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeFetchOmzetInteractor @Inject constructor(
    private val homeRepository: IHomeRepository
) : HomeFetchOmzetUseCase {
    override suspend fun fetchOmzetMonthly(month: Int, year: Int): Flow<Resource<out Omzet?>> =
        homeRepository.fetchOmzetMonthly(month, year)
}