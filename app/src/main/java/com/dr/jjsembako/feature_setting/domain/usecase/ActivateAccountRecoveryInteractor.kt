package com.dr.jjsembako.feature_setting.domain.usecase

import com.dr.jjsembako.feature_setting.domain.repository.IRecoveryRepository
import javax.inject.Inject

class ActivateAccountRecoveryInteractor @Inject constructor(private val recoveryRepository: IRecoveryRepository) :
    ActivateAccountRecoveryUseCase {
    override suspend fun handleActivateAccountRecovery(idQuestion: String, answer: String) =
        recoveryRepository.handleActivateAccountRecovery(idQuestion, answer)
}