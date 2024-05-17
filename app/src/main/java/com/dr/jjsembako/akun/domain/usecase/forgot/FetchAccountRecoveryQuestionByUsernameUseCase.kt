package com.dr.jjsembako.akun.domain.usecase.forgot

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataAccountRecoveryQuestion
import kotlinx.coroutines.flow.Flow

interface FetchAccountRecoveryQuestionByUsernameUseCase {

    suspend fun fetchAccountRecoveryQuestionByUsername(username: String): Flow<Resource<out DataAccountRecoveryQuestion?>>

}