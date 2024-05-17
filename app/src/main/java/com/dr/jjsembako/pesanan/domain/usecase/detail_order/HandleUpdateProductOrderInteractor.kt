package com.dr.jjsembako.pesanan.domain.usecase.detail_order

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.PatchHandleUpdateProductOrderResponse
import com.dr.jjsembako.pesanan.domain.repository.IHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HandleUpdateProductOrderInteractor @Inject constructor(private val historyRepository: IHistoryRepository) :
    HandleUpdateProductOrderUseCase {
    override suspend fun handleUpdateProductOrder(
        id: String,
        productId: String,
        amountInUnit: Int,
        pricePerUnit: Long
    ): Flow<Resource<out PatchHandleUpdateProductOrderResponse>> =
        historyRepository.handleUpdateProductOrder(
            id,
            productId,
            amountInUnit,
            pricePerUnit
        )
}