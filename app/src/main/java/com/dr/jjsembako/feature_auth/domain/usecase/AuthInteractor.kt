package com.dr.jjsembako.feature_auth.domain.usecase

import com.dr.jjsembako.feature_auth.domain.repository.IAuthRepository
import javax.inject.Inject

class AuthInteractor @Inject constructor(private val authRepository: IAuthRepository) :
    AuthUseCase {
}