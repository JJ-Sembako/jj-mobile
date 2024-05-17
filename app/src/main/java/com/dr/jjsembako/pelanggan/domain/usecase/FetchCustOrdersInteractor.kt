package com.dr.jjsembako.pelanggan.domain.usecase

import androidx.paging.PagingData
import com.dr.jjsembako.pesanan.domain.model.Order
import com.dr.jjsembako.pelanggan.domain.repository.ICustomerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchCustOrdersInteractor @Inject constructor(private val customerRepository: ICustomerRepository) :
    FetchCustOrdersUseCase {

    override suspend fun fetchOrders(
        search: String?,
        minDate: String?,
        maxDate: String?,
        me: Int?,
        customerId: String
    ): Flow<PagingData<Order>> = customerRepository.fetchOrders(
        search = search,
        minDate = minDate,
        maxDate = maxDate,
        me = me,
        customerId = customerId
    )
}