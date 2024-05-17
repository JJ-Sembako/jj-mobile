package com.dr.jjsembako.akun.domain.usecase.recovery

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataAccountRecovery
import kotlinx.coroutines.flow.Flow

interface GetDataAccountRecoveryUseCase {

    suspend fun fetchAccountRecovery(): Flow<Resource<out DataAccountRecovery?>>

}