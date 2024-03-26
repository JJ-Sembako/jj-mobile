package com.dr.jjsembako.feature_history.domain.usecase.order

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.DetailOrder
import com.dr.jjsembako.feature_history.domain.repository.IHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchOrderInteractor @Inject constructor(private val historyRepository: IHistoryRepository) :
    FetchOrderUseCase {

    override suspend fun fetchOrder(id: String): Flow<Resource<out DetailOrder?>> =
        historyRepository.fetchOrder(id)
}