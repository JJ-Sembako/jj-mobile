package com.dr.jjsembako.pesanan.domain.usecase.canceled

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.canceled.DeleteHandleDeleteCanceledResponse
import kotlinx.coroutines.flow.Flow

interface HandleDeleteCanceledUseCase {

    suspend fun handleDeleteCanceled(id: String): Flow<Resource<out DeleteHandleDeleteCanceledResponse>>

}