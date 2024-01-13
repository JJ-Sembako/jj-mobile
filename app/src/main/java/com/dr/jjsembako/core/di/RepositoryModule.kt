package com.dr.jjsembako.core.di

import com.dr.jjsembako.feature_auth.data.AuthRepository
import com.dr.jjsembako.feature_auth.domain.repository.IAuthRepository
import com.dr.jjsembako.feature_auth.domain.repository.IForgetPasswordRepository
import com.dr.jjsembako.feature_customer.data.CustomerRepository
import com.dr.jjsembako.feature_customer.domain.repository.ICustomerRepository
import com.dr.jjsembako.feature_order.data.OrderRepository
import com.dr.jjsembako.feature_order.domain.repository.ISelectCustRepository
import com.dr.jjsembako.feature_setting.data.SettingRepository
import com.dr.jjsembako.feature_setting.domain.repository.IRecoveryRepository
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
    abstract fun provideRepositoryForgetPassword(authRepository: AuthRepository): IForgetPasswordRepository

    @Binds
    abstract fun provideRepositorySetting(settingRepository: SettingRepository): ISettingRepository

    @Binds
    abstract fun provideRepositoryRecovery(settingRepository: SettingRepository): IRecoveryRepository

    @Binds
    abstract fun provideCustomerRepository(customerRepository: CustomerRepository): ICustomerRepository

    @Binds
    abstract fun provideSelectCustomerRepository(orderRepository: OrderRepository): ISelectCustRepository

}