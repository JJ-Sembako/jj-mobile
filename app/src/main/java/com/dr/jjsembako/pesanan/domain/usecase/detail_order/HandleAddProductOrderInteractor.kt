package com.dr.jjsembako.pesanan.domain.usecase.detail_order

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.PostHandleAddProductOrderResponse
import com.dr.jjsembako.pesanan.domain.repository.IHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HandleAddProductOrderInteractor @Inject constructor(private val historyRepository: IHistoryRepository) :
    HandleAddProductOrderUseCase {
    override suspend fun handleAddProductOrder(
        id: String,
        productId: String,
        amountInUnit: Int,
        pricePerUnit: Long
    ): Flow<Resource<out PostHandleAddProductOrderResponse>> =
        historyRepository.handleAddProductOrder(
            id,
            productId,
            amountInUnit,
            pricePerUnit
        )
}