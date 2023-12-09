package com.dr.jjsembako.feature_auth.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleUpdatePasswordFromRecoveryResponse
import kotlinx.coroutines.flow.Flow

interface HandleUpdatePasswordFromRecoveryUseCase {

    suspend fun handleUpdatePasswordFromRecovery(
        username: String,
        newPassword: String,
        confNewPassword: String
    ): Flow<Resource<out PatchHandleUpdatePasswordFromRecoveryResponse>>

}