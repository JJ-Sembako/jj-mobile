package com.dr.jjsembako.pesanan.domain.usecase.detail_order

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.PatchHandleUpdatePaymentStatusResponse
import kotlinx.coroutines.flow.Flow

interface HandleUpdatePaymentStatusUseCase {

    suspend fun handleUpdatePaymentStatus(id: String): Flow<Resource<out PatchHandleUpdatePaymentStatusResponse>>

}