package com.dr.jjsembako.akun.domain.usecase.forgot

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataCheckAccountRecoveryAnswer
import com.dr.jjsembako.akun.domain.repository.IForgetPasswordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckAccountRecoveryAnswerInteractor @Inject constructor(private val fpRepository: IForgetPasswordRepository) :
    CheckAccountRecoveryAnswerUseCase {
    override suspend fun checkAccountRecoveryAnswer(
        username: String,
        answer: String
    ): Flow<Resource<out DataCheckAccountRecoveryAnswer?>> =
        fpRepository.checkAccountRecoveryAnswer(username, answer)
}