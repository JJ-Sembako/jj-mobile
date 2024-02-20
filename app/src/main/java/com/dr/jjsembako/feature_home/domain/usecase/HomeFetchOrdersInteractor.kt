package com.dr.jjsembako.feature_home.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.OrderDataItem
import com.dr.jjsembako.feature_home.domain.repository.IHomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeFetchOrdersInteractor @Inject constructor(
    private val homeRepository: IHomeRepository
) : HomeFetchOrdersUseCase {
    override suspend fun fetchOrders(
        limit: Int?,
        me: Int?
    ): Flow<Resource<out List<OrderDataItem?>?>> = homeRepository.fetchOrders(limit, me)
}