package com.dr.jjsembako.feature_setting.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleDeactivateAccountRecoveryResponse
import kotlinx.coroutines.flow.Flow

interface DeactivateAccountRecoveryUseCase {

    suspend fun handleDeactivateAccountRecovery(): Flow<Resource<out PatchHandleDeactivateAccountRecoveryResponse>>

}