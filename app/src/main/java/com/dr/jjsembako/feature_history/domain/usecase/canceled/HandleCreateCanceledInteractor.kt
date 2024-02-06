package com.dr.jjsembako.feature_history.domain.usecase.canceled

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.canceled.PostHandleCreateCanceledResponse
import com.dr.jjsembako.feature_history.domain.repository.ICanceledRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HandleCreateCanceledInteractor @Inject constructor(private val canceledRepository: ICanceledRepository) :
    HandleCreateCanceledUseCase {
    override suspend fun handleCreateCanceled(
        orderId: String,
        productId: String,
        amountInUnit: Int
    ): Flow<Resource<out PostHandleCreateCanceledResponse>> =
        canceledRepository.handleCreateCanceled(
            orderId = orderId,
            productId = productId,
            amountInUnit = amountInUnit
        )
}