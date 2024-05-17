package com.dr.jjsembako.pesanan.domain.usecase.canceled

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.canceled.DeleteHandleDeleteCanceledResponse
import com.dr.jjsembako.pesanan.domain.repository.ICanceledRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HandleDeleteCanceledInteractor @Inject constructor(private val canceledRepository: ICanceledRepository) :
    HandleDeleteCanceledUseCase {
    override suspend fun handleDeleteCanceled(id: String): Flow<Resource<out DeleteHandleDeleteCanceledResponse>> =
        canceledRepository.handleDeleteCanceled(id)
}