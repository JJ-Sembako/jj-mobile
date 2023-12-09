package com.dr.jjsembako.feature_setting.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataActivateAccountRecovery
import kotlinx.coroutines.flow.Flow

interface ActivateAccountRecoveryUseCase {

    suspend fun handleActivateAccountRecovery(
        idQuestion: String,
        answer: String
    ): Flow<Resource<out DataActivateAccountRecovery?>>

}