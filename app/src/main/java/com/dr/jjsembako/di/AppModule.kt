package com.dr.jjsembako.di

import com.dr.jjsembako.feature_auth.domain.usecase.LoginInteractor
import com.dr.jjsembako.feature_auth.domain.usecase.LoginUseCase
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
}