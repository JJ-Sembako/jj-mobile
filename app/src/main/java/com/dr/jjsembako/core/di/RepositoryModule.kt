package com.dr.jjsembako.core.di

import com.dr.jjsembako.feature_auth.data.AuthRepository
import com.dr.jjsembako.feature_auth.domain.repository.IAuthRepository
import com.dr.jjsembako.feature_auth.domain.repository.IForgetPasswordRepository
import com.dr.jjsembako.feature_customer.data.CustomerRepository
import com.dr.jjsembako.feature_customer.domain.repository.ICustomerRepository
import com.dr.jjsembako.feature_history.data.HistoryRepository
import com.dr.jjsembako.feature_history.domain.repository.ICanceledRepository
import com.dr.jjsembako.feature_history.domain.repository.IHistoryRepository
import com.dr.jjsembako.feature_history.domain.repository.IReturRepository
import com.dr.jjsembako.feature_order.data.OrderRepository
import com.dr.jjsembako.feature_order.domain.repository.IOrderRepository
import com.dr.jjsembako.feature_order.domain.repository.ISelectCustRepository
import com.dr.jjsembako.feature_performance.data.PerformanceRepository
import com.dr.jjsembako.feature_performance.domain.repository.IPerformanceRepository
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
    abstract fun provideAuthRepository(authRepository: AuthRepository): IAuthRepository

    @Binds
    abstract fun provideForgetPasswordRepository(authRepository: AuthRepository): IForgetPasswordRepository

    @Binds
    abstract fun provideSettingRepository(settingRepository: SettingRepository): ISettingRepository

    @Binds
    abstract fun provideRecoveryRepository(settingRepository: SettingRepository): IRecoveryRepository

    @Binds
    abstract fun provideCustomerRepository(customerRepository: CustomerRepository): ICustomerRepository

    @Binds
    abstract fun provideOrderRepository(orderRepository: OrderRepository): IOrderRepository

    @Binds
    abstract fun provideSelectCustomerRepository(orderRepository: OrderRepository): ISelectCustRepository

    @Binds
    abstract fun provideHistoryRepository(historyRepository: HistoryRepository): IHistoryRepository

    @Binds
    abstract fun provideCanceledRepository(historyRepository: HistoryRepository): ICanceledRepository

    @Binds
    abstract fun provideReturRepository(historyRepository: HistoryRepository): IReturRepository

    @Binds
    abstract fun providePerformanceRepository(performanceRepository: PerformanceRepository): IPerformanceRepository

}