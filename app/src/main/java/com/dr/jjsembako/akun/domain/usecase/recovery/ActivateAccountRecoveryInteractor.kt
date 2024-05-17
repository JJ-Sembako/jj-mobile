package com.dr.jjsembako.akun.domain.usecase.recovery

import com.dr.jjsembako.akun.domain.repository.IRecoveryRepository
import javax.inject.Inject

class ActivateAccountRecoveryInteractor @Inject constructor(private val recoveryRepository: IRecoveryRepository) :
    ActivateAccountRecoveryUseCase {
    override suspend fun handleActivateAccountRecovery(idQuestion: String, answer: String) =
        recoveryRepository.handleActivateAccountRecovery(idQuestion, answer)
}