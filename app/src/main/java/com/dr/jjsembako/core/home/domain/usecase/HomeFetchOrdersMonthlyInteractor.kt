package com.dr.jjsembako.core.home.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.home.domain.repository.IHomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeFetchOrdersMonthlyInteractor @Inject constructor(
    private val homeRepository: IHomeRepository
) : HomeFetchOrdersMonthlyUseCase {
    override suspend fun fetchOrders(
        minDate: String?,
        maxDate: String?,
        me: Int?
    ): Flow<Resource<out Int?>> = homeRepository.fetchOrders(minDate, maxDate, me)
}