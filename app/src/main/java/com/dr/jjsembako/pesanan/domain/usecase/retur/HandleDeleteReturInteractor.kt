package com.dr.jjsembako.pesanan.domain.usecase.retur

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.retur.DeleteHandleDeleteReturResponse
import com.dr.jjsembako.pesanan.domain.repository.IReturRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HandleDeleteReturInteractor @Inject constructor(private val returRepository: IReturRepository) :
    HandleDeleteReturUseCase {
    override suspend fun handleDeleteRetur(id: String): Flow<Resource<out DeleteHandleDeleteReturResponse>> =
        returRepository.handleDeleteRetur(id)
}