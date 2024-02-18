package com.dr.jjsembako.feature_history.domain.usecase.order

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.DeleteHandleDeleteProductOrderResponse
import com.dr.jjsembako.feature_history.domain.repository.IHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HandleDeleteProductOrderInteractor @Inject constructor(private val historyRepository: IHistoryRepository) :
    HandleDeleteProductOrderUseCase {
    override suspend fun handleDeleteProductOrder(
        id: String,
        productId: String
    ): Flow<Resource<out DeleteHandleDeleteProductOrderResponse>> =
        historyRepository.handleDeleteProductOrder(id, productId)
}