package com.dr.jjsembako.akun.domain.usecase.forgot

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleUpdatePasswordFromRecoveryResponse
import com.dr.jjsembako.akun.domain.repository.IForgetPasswordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HandleUpdatePasswordFromRecoveryInteractor @Inject constructor(private val fpRepository: IForgetPasswordRepository) :
    HandleUpdatePasswordFromRecoveryUseCase {
    override suspend fun handleUpdatePasswordFromRecovery(
        username: String,
        newPassword: String,
        confNewPassword: String
    ): Flow<Resource<out PatchHandleUpdatePasswordFromRecoveryResponse>> =
        fpRepository.handleUpdatePasswordFromRecovery(username, newPassword, confNewPassword)
}