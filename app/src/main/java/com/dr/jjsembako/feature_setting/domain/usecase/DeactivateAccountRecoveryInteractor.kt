package com.dr.jjsembako.feature_setting.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleDeactivateAccountRecoveryResponse
import com.dr.jjsembako.feature_setting.domain.repository.IRecoveryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeactivateAccountRecoveryInteractor @Inject constructor(private val recoveryRepository: IRecoveryRepository) :
    DeactivateAccountRecoveryUseCase {
    override suspend fun handleDeactivateAccountRecovery(): Flow<Resource<out PatchHandleDeactivateAccountRecoveryResponse>> =
        recoveryRepository.handleDeactivateAccountRecovery()
}