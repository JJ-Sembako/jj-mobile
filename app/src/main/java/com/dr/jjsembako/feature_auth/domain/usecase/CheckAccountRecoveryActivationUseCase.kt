package com.dr.jjsembako.feature_auth.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataCheckAccountRecoveryActivation
import kotlinx.coroutines.flow.Flow

interface CheckAccountRecoveryActivationUseCase {

    suspend fun checkAccountRecoveryActivation(username: String): Flow<Resource<out DataCheckAccountRecoveryActivation?>>

}