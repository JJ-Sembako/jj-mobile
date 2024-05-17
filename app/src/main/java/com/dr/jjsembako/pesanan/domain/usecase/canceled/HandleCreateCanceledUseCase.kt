package com.dr.jjsembako.pesanan.domain.usecase.canceled

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.canceled.PostHandleCreateCanceledResponse
import kotlinx.coroutines.flow.Flow

interface HandleCreateCanceledUseCase {

    suspend fun handleCreateCanceled(
        orderId: String,
        productId: String,
        amountInUnit: Int
    ): Flow<Resource<out PostHandleCreateCanceledResponse>>

}