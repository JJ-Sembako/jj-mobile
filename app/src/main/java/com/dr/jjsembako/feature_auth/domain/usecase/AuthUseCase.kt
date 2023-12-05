package com.dr.jjsembako.feature_auth.domain.usecase

import android.accounts.Account
import com.dr.jjsembako.core.common.Resource
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {

    suspend fun handleLogin(username: String, password: String): Flow<Resource<Account>>

}