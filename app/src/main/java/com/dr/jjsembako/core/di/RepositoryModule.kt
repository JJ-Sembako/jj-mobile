package com.dr.jjsembako.core.di

import com.dr.jjsembako.feature_auth.data.AuthRepository
import com.dr.jjsembako.feature_auth.domain.repository.IAuthRepository
import com.dr.jjsembako.feature_customer.domain.repository.ICustomerRepository
import com.dr.jjsembako.feature_setting.data.SettingRepository
import com.dr.jjsembako.feature_setting.domain.repository.ISettingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideRepositoryAuth(authRepository: AuthRepository): IAuthRepository

    @Binds
    abstract fun provideRepositorySetting(settingRepository: SettingRepository): ISettingRepository

}