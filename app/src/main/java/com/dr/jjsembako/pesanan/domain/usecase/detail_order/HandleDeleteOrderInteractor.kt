package com.dr.jjsembako.pesanan.domain.usecase.detail_order

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.DeleteHandleDeleteOrderResponse
import com.dr.jjsembako.pesanan.domain.repository.IHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HandleDeleteOrderInteractor @Inject constructor(private val historyRepository: IHistoryRepository) :
    HandleDeleteOrderUseCase {
    override suspend fun handleDeleteOrder(id: String): Flow<Resource<out DeleteHandleDeleteOrderResponse>> =
        historyRepository.handleDeleteOrder(id)
}