package com.dr.jjsembako.feature_setting.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataAccountRecovery
import com.dr.jjsembako.feature_setting.domain.repository.IRecoveryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDataAccountRecoveryInteractor @Inject constructor(private val recoveryRepository: IRecoveryRepository) :
    GetDataAccountRecoveryUSeCase {
    override suspend fun fetchAccountRecovery(): Flow<Resource<out DataAccountRecovery?>> =
        recoveryRepository.fetchAccountRecovery()

}