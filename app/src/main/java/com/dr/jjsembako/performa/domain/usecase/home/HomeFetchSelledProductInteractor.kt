package com.dr.jjsembako.performa.domain.usecase.home

import com.dr.jjsembako.performa.domain.repository.IHomeRepository
import javax.inject.Inject

class HomeFetchSelledProductInteractor @Inject constructor(
    private val homeRepository: IHomeRepository
) : HomeFetchSelledProductUseCase {
    override suspend fun fetchSelledProductMonthly(month: Int, year: Int) =
        homeRepository.fetchSelledProductMonthly(month, year)
}