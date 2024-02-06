package com.dr.jjsembako.feature_history.domain.usecase.order

import androidx.paging.PagingData
import com.dr.jjsembako.core.data.remote.response.order.OrderDataItem
import com.dr.jjsembako.feature_history.domain.repository.IHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchOrdersInteractor @Inject constructor(private val historyRepository: IHistoryRepository) :
    FetchOrdersUseCase {

    override suspend fun fetchOrders(
        search: String?,
        minDate: String?,
        maxDate: String?,
        me: Int?
    ): Flow<PagingData<OrderDataItem>> = historyRepository.fetchOrders(
        search = search,
        minDate = minDate,
        maxDate = maxDate,
        me = me
    )
}