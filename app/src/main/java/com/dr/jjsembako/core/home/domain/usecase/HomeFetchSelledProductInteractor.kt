package com.dr.jjsembako.core.home.domain.usecase

import com.dr.jjsembako.core.home.domain.repository.IHomeRepository
import javax.inject.Inject

class HomeFetchSelledProductInteractor @Inject constructor(
    private val homeRepository: IHomeRepository
) : HomeFetchSelledProductUseCase {
    override suspend fun fetchSelledProductMonthly(month: Int, year: Int) =
        homeRepository.fetchSelledProductMonthly(month, year)
}