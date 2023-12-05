package com.dr.jjsembako.feature_auth.presentation.login

import androidx.lifecycle.ViewModel
import com.dr.jjsembako.feature_auth.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(loginUseCase: LoginUseCase): ViewModel() {
}