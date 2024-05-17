package com.dr.jjsembako.pesanan.domain.usecase.create

import com.dr.jjsembako.core.data.model.OrderRequest
import com.dr.jjsembako.pesanan.domain.repository.IOrderRepository
import javax.inject.Inject

class HandleCreateOrderInteractor @Inject constructor(private val orderRepository: IOrderRepository) :
    HandleCreateOrderUseCase {
    override suspend fun handleCreateOrder(orderRequest: OrderRequest) =
        orderRepository.handleCreateOrder(orderRequest)
}