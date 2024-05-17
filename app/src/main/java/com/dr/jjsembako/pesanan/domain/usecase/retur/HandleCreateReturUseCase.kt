package com.dr.jjsembako.pesanan.domain.usecase.retur

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.retur.PostHandleCreateReturResponse
import kotlinx.coroutines.flow.Flow

interface HandleCreateReturUseCase {

    suspend fun handleCreateRetur(
        orderId: String,
        returedProductId: String,
        returnedProductId: String,
        amountInUnit: Int,
        selledPrice: Long
    ): Flow<Resource<out PostHandleCreateReturResponse>>

}