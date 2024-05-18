package com.dr.jjsembako.performa.domain.usecase.home

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.pesanan.domain.model.Order
import com.dr.jjsembako.performa.domain.repository.IHomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeFetchOrdersInteractor @Inject constructor(
    private val homeRepository: IHomeRepository
) : HomeFetchOrdersUseCase {
    override suspend fun fetchOrders(
        limit: Int?,
        me: Int?
    ): Flow<Resource<out List<Order?>?>> = homeRepository.fetchOrders(limit, me)
}