package com.dr.jjsembako.akun.domain.usecase.forgot

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataCheckAccountRecoveryActivation
import com.dr.jjsembako.akun.domain.repository.IForgetPasswordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckAccountRecoveryActivationInteractor @Inject constructor(private val fpRepository: IForgetPasswordRepository) :
    CheckAccountRecoveryActivationUseCase {
    override suspend fun checkAccountRecoveryActivation(username: String): Flow<Resource<out DataCheckAccountRecoveryActivation?>> =
        fpRepository.checkAccountRecoveryActivation(username)
}