package com.dr.jjsembako.akun.domain.usecase.recovery

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataAccountRecovery
import com.dr.jjsembako.akun.domain.repository.IRecoveryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDataAccountRecoveryInteractor @Inject constructor(private val recoveryRepository: IRecoveryRepository) :
    GetDataAccountRecoveryUseCase {
    override suspend fun fetchAccountRecovery(): Flow<Resource<out DataAccountRecovery?>> =
        recoveryRepository.fetchAccountRecovery()

}