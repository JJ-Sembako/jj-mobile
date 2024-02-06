package com.dr.jjsembako.feature_history.domain.usecase.retur

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.retur.PostHandleCreateReturResponse
import com.dr.jjsembako.feature_history.domain.repository.IReturRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HandleCreateReturInteractor @Inject constructor(private val returRepository: IReturRepository) :
    HandleCreateReturUseCase {
    override suspend fun handleCreateRetur(
        orderId: String,
        returedProductId: String,
        returnedProductId: String,
        amountInUnit: Int,
        selledPrice: Long
    ): Flow<Resource<out PostHandleCreateReturResponse>> = returRepository.handleCreateRetur(
        orderId = orderId,
        returedProductId = returedProductId,
        returnedProductId = returnedProductId,
        amountInUnit = amountInUnit,
        selledPrice = selledPrice
    )
}