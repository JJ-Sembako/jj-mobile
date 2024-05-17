package com.dr.jjsembako.pesanan.domain.usecase.detail_order

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.pesanan.domain.model.DetailOrder
import com.dr.jjsembako.pesanan.domain.repository.IHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchOrderInteractor @Inject constructor(private val historyRepository: IHistoryRepository) :
    FetchOrderUseCase {

    override suspend fun fetchOrder(id: String): Flow<Resource<out DetailOrder?>> =
        historyRepository.fetchOrder(id)
}