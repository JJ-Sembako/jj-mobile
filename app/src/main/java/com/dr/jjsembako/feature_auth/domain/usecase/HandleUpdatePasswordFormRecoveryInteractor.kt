package com.dr.jjsembako.feature_auth.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleUpdatePasswordFormRecoveryResponse
import com.dr.jjsembako.feature_auth.domain.repository.IAuthRepository
import com.dr.jjsembako.feature_auth.domain.repository.IForgetPasswordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HandleUpdatePasswordFormRecoveryInteractor @Inject constructor(private val fpRepository: IForgetPasswordRepository) :
    HandleUpdatePasswordFormRecoveryUseCase {
    override suspend fun handleUpdatePasswordFromRecovery(
        username: String,
        newPassword: String,
        confNewPassword: String
    ): Flow<Resource<out PatchHandleUpdatePasswordFormRecoveryResponse>> =
        fpRepository.handleUpdatePasswordFromRecovery(username, newPassword, confNewPassword)
}