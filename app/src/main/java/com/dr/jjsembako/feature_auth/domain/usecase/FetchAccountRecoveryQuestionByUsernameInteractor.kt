package com.dr.jjsembako.feature_auth.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataAccountRecoveryQuestion
import com.dr.jjsembako.feature_auth.domain.repository.IForgetPasswordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchAccountRecoveryQuestionByUsernameInteractor @Inject constructor(private val fpRepository: IForgetPasswordRepository) :
    FetchAccountRecoveryQuestionByUsernameUseCase {
    override suspend fun fetchAccountRecoveryQuestionByUsername(username: String): Flow<Resource<out DataAccountRecoveryQuestion?>> =
        fpRepository.fetchAccountRecoveryQuestionByUsername(username)
}