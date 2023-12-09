package com.dr.jjsembako.di

import com.dr.jjsembako.feature_auth.domain.usecase.LoginInteractor
import com.dr.jjsembako.feature_auth.domain.usecase.LoginUseCase
import com.dr.jjsembako.feature_setting.domain.usecase.ActivateAccountRecoveryInteractor
import com.dr.jjsembako.feature_setting.domain.usecase.ActivateAccountRecoveryUseCase
import com.dr.jjsembako.feature_setting.domain.usecase.ChangePasswordInteractor
import com.dr.jjsembako.feature_setting.domain.usecase.ChangePasswordUseCase
import com.dr.jjsembako.feature_setting.domain.usecase.DeactivateAccountRecoveryInteractor
import com.dr.jjsembako.feature_setting.domain.usecase.DeactivateAccountRecoveryUseCase
import com.dr.jjsembako.feature_setting.domain.usecase.GetAllRecoveryQuestionInteractor
import com.dr.jjsembako.feature_setting.domain.usecase.GetAllRecoveryQuestionUseCase
import com.dr.jjsembako.feature_setting.domain.usecase.GetDataAccountRecoveryInteractor
import com.dr.jjsembako.feature_setting.domain.usecase.GetDataAccountRecoveryUSeCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    /****************************
     * Account
     ***************************/
    @Binds
    @ViewModelScoped
    abstract fun provideLoginUseCase(loginInteractor: LoginInteractor): LoginUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideChangePasswordUseCase(changePasswordInteractor: ChangePasswordInteractor): ChangePasswordUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetAllRecoveryQuestionUseCase(getAllRecoveryQuestionInteractor: GetAllRecoveryQuestionInteractor): GetAllRecoveryQuestionUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetDataAccountRecoveryUSeCase(getDataAccountRecoveryInteractor: GetDataAccountRecoveryInteractor): GetDataAccountRecoveryUSeCase

    @Binds
    @ViewModelScoped
    abstract fun provideActivateAccountRecoveryUseCase(activateAccountRecoveryInteractor: ActivateAccountRecoveryInteractor): ActivateAccountRecoveryUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideDeactivateAccountRecoveryUseCase(deactivateAccountRecoveryInteractor: DeactivateAccountRecoveryInteractor): DeactivateAccountRecoveryUseCase
}