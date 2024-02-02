package com.dr.jjsembako.feature_order.domain.usecase

import com.dr.jjsembako.core.data.model.OrderProduct
import com.dr.jjsembako.feature_order.domain.repository.IOrderRepository
import javax.inject.Inject

class HandleCreateOrderInteractor @Inject constructor(private val orderRepository: IOrderRepository) :
    HandleCreateOrderUseCase {
    override suspend fun handleCreateOrder(
        customerId: String,
        products: List<OrderProduct>,
        paymentStatus: String
    ) = orderRepository.handleCreateOrder(customerId, products, paymentStatus)
}