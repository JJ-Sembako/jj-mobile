package com.dr.jjsembako.akun.domain.usecase.recovery

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataRecoveryQuestion
import com.dr.jjsembako.akun.domain.repository.IRecoveryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllRecoveryQuestionInteractor @Inject constructor(private val recoveryRepository: IRecoveryRepository) :
    GetAllRecoveryQuestionUseCase {
    override suspend fun fetchAccountRecoveryQuestions(): Flow<Resource<out List<DataRecoveryQuestion?>>> =
        recoveryRepository.fetchAccountRecoveryQuestions()
}