package com.dr.jjsembako.pesanan.domain.usecase.detail_order

import androidx.paging.PagingData
import com.dr.jjsembako.pesanan.domain.model.Order
import com.dr.jjsembako.pesanan.domain.repository.IHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchOrdersInteractor @Inject constructor(private val historyRepository: IHistoryRepository) :
    FetchOrdersUseCase {

    override suspend fun fetchOrders(
        search: String?,
        minDate: String?,
        maxDate: String?,
        me: Int?
    ): Flow<PagingData<Order>> = historyRepository.fetchOrders(
        search = search,
        minDate = minDate,
        maxDate = maxDate,
        me = me
    )
}