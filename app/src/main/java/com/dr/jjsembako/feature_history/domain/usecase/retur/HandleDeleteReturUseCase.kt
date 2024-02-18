package com.dr.jjsembako.feature_history.domain.usecase.retur

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.retur.DeleteHandleDeleteReturResponse
import kotlinx.coroutines.flow.Flow

interface HandleDeleteReturUseCase {

    suspend fun handleDeleteRetur(id: String): Flow<Resource<out DeleteHandleDeleteReturResponse>>

}