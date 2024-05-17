package com.dr.jjsembako.akun.domain.usecase.auth

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataHandleLogin
import kotlinx.coroutines.flow.Flow

interface LoginUseCase {

    suspend fun handleLogin(username: String, password: String): Flow<Resource<out DataHandleLogin?>>

}