package com.dr.jjsembako.feature_auth.domain.repository

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataAccountRecoveryQuestion
import com.dr.jjsembako.core.data.remote.response.account.DataCheckAccountRecoveryActivation
import com.dr.jjsembako.core.data.remote.response.account.DataCheckAccountRecoveryAnswer
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleUpdatePasswordFromRecoveryResponse
import kotlinx.coroutines.flow.Flow

interface IForgetPasswordRepository {

    suspend fun checkAccountRecoveryActivation(username: String): Flow<Resource<out DataCheckAccountRecoveryActivation?>>

    suspend fun fetchAccountRecoveryQuestionByUsername(username: String): Flow<Resource<out DataAccountRecoveryQuestion?>>

    suspend fun checkAccountRecoveryAnswer(
        username: String,
        answer: String
    ): Flow<Resource<out DataCheckAccountRecoveryAnswer?>>

    suspend fun handleUpdatePasswordFromRecovery(
        username: String,
        newPassword: String,
        confNewPassword: String
    ): Flow<Resource<out PatchHandleUpdatePasswordFromRecoveryResponse>>

}