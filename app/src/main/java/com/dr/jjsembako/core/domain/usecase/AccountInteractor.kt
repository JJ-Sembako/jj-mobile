package com.dr.jjsembako.core.domain.usecase

import com.dr.jjsembako.core.domain.repository.IAccountRepository
import javax.inject.Inject

class AccountInteractor @Inject constructor(private val accountRepository: IAccountRepository) :
    AccountUseCase {
}