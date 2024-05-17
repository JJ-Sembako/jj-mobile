package com.dr.jjsembako.akun.domain.usecase.recovery

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleDeactivateAccountRecoveryResponse
import com.dr.jjsembako.akun.domain.repository.IRecoveryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeactivateAccountRecoveryInteractor @Inject constructor(private val recoveryRepository: IRecoveryRepository) :
    DeactivateAccountRecoveryUseCase {
    override suspend fun handleDeactivateAccountRecovery(): Flow<Resource<out PatchHandleDeactivateAccountRecoveryResponse>> =
        recoveryRepository.handleDeactivateAccountRecovery()
}