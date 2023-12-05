package com.dr.jjsembako.feature_auth.domain.usecase

import android.accounts.Account
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.common.UiState
import com.dr.jjsembako.feature_auth.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthInteractor @Inject constructor(private val authRepository: IAuthRepository) :
    AuthUseCase {
    override suspend fun handleLogin(username: String, password: String): Flow<Resource<Account>> =
        authRepository.handleLogin(username, password)
}