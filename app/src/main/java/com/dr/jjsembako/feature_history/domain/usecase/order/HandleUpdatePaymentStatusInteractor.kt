package com.dr.jjsembako.feature_history.domain.usecase.order

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.PatchHandleUpdatePaymentStatusResponse
import com.dr.jjsembako.feature_history.domain.repository.IHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HandleUpdatePaymentStatusInteractor @Inject constructor(private val historyRepository: IHistoryRepository) :
    HandleUpdatePaymentStatusUseCase {
    override suspend fun handleUpdatePaymentStatus(id: String): Flow<Resource<out PatchHandleUpdatePaymentStatusResponse>> =
        historyRepository.handleUpdatePaymentStatus(id)
}