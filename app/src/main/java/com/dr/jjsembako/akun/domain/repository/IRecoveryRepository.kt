package com.dr.jjsembako.akun.domain.repository

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataAccountRecovery
import com.dr.jjsembako.core.data.remote.response.account.DataActivateAccountRecovery
import com.dr.jjsembako.core.data.remote.response.account.DataRecoveryQuestion
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleDeactivateAccountRecoveryResponse
import kotlinx.coroutines.flow.Flow

interface IRecoveryRepository {

    suspend fun fetchAccountRecoveryQuestions(): Flow<Resource<out List<DataRecoveryQuestion?>>>

    suspend fun fetchAccountRecovery(): Flow<Resource<out DataAccountRecovery?>>

    suspend fun handleActivateAccountRecovery(
        idQuestion: String,
        answer: String
    ): Flow<Resource<out DataActivateAccountRecovery?>>

    suspend fun handleDeactivateAccountRecovery(): Flow<Resource<out PatchHandleDeactivateAccountRecoveryResponse>>

}