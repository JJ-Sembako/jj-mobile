package com.dr.jjsembako.pesanan.domain.repository

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.canceled.DeleteHandleDeleteCanceledResponse
import com.dr.jjsembako.core.data.remote.response.canceled.PostHandleCreateCanceledResponse
import kotlinx.coroutines.flow.Flow

interface ICanceledRepository {

    suspend fun handleCreateCanceled(
        orderId: String,
        productId: String,
        amountInUnit: Int
    ): Flow<Resource<out PostHandleCreateCanceledResponse>>

    suspend fun handleDeleteCanceled(id: String): Flow<Resource<out DeleteHandleDeleteCanceledResponse>>

}