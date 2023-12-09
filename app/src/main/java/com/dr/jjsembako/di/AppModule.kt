package com.dr.jjsembako.di

import com.dr.jjsembako.feature_auth.domain.usecase.CheckAccountRecoveryActivationInteractor
import com.dr.jjsembako.feature_auth.domain.usecase.CheckAccountRecoveryActivationUseCase
import com.dr.jjsembako.feature_auth.domain.usecase.CheckAccountRecoveryAnswerInteractor
import com.dr.jjsembako.feature_auth.domain.usecase.CheckAccountRecoveryAnswerUseCase
import com.dr.jjsembako.feature_auth.domain.usecase.FetchAccountRecoveryQuestionByUsernameInteractor
import com.dr.jjsembako.feature_auth.domain.usecase.FetchAccountRecoveryQuestionByUsernameUseCase
import com.dr.jjsembako.feature_auth.domain.usecase.HandleUpdatePasswordFromRecoveryUseCase
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

    @Binds
    @ViewModelScoped
    abstract fun provideCheckAccountRecoveryActivationUseCase(
        checkAccountRecoveryActivationInteractor: CheckAccountRecoveryActivationInteractor
    ): CheckAccountRecoveryActivationUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideFetchAccountRecoveryQuestionByUsernameUseCase(
        fetchAccountRecoveryQuestionByUsernameInteractor: FetchAccountRecoveryQuestionByUsernameInteractor
    ): FetchAccountRecoveryQuestionByUsernameUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideCheckAccountRecoveryAnswerUseCase(checkAccountRecoveryAnswerInteractor: CheckAccountRecoveryAnswerInteractor): CheckAccountRecoveryAnswerUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHandleUpdatePasswordFromRecoveryUseCase(
        hanldeUpdatePasswordFromRecoveryInteractor: DeactivateAccountRecoveryInteractor
    ): HandleUpdatePasswordFromRecoveryUseCase
}