package com.dr.jjsembako.akun.domain.usecase.auth

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataHandleLogin
import com.dr.jjsembako.akun.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginInteractor @Inject constructor(private val authRepository: IAuthRepository) :
    LoginUseCase {
    override suspend fun handleLogin(
        username: String,
        password: String
    ): Flow<Resource<out DataHandleLogin?>> =
        authRepository.handleLogin(username, password)
}