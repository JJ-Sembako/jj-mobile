package com.dr.jjsembako.akun.domain.usecase.recovery

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataRecoveryQuestion
import kotlinx.coroutines.flow.Flow

interface GetAllRecoveryQuestionUseCase {

    suspend fun fetchAccountRecoveryQuestions(): Flow<Resource<out List<DataRecoveryQuestion?>>>

}