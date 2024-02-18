package com.dr.jjsembako.feature_history.domain.usecase.order

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.PatchHandleUpdatePaymentStatusResponse
import kotlinx.coroutines.flow.Flow

interface HandleUpdatePaymentStatusUseCase {

    suspend fun handleUpdatePaymentStatus(id: String): Flow<Resource<out PatchHandleUpdatePaymentStatusResponse>>

}