package com.dr.jjsembako.core.di

import com.dr.jjsembako.akun.data.AuthRepository
import com.dr.jjsembako.akun.domain.repository.IAuthRepository
import com.dr.jjsembako.akun.domain.repository.IForgetPasswordRepository
import com.dr.jjsembako.pelanggan.data.CustomerRepository
import com.dr.jjsembako.pelanggan.domain.repository.ICustomerRepository
import com.dr.jjsembako.pesanan.data.HistoryRepository
import com.dr.jjsembako.pesanan.domain.repository.ICanceledRepository
import com.dr.jjsembako.pesanan.domain.repository.IHistoryRepository
import com.dr.jjsembako.pesanan.domain.repository.IReturRepository
import com.dr.jjsembako.performa.data.HomeRepository
import com.dr.jjsembako.performa.domain.repository.IHomeRepository
import com.dr.jjsembako.pesanan.data.OrderRepository
import com.dr.jjsembako.pesanan.domain.repository.IOrderRepository
import com.dr.jjsembako.pesanan.domain.repository.ISelectCustRepository
import com.dr.jjsembako.performa.data.PerformanceRepository
import com.dr.jjsembako.performa.domain.repository.IPerformanceRepository
import com.dr.jjsembako.akun.data.SettingRepository
import com.dr.jjsembako.akun.domain.repository.IRecoveryRepository
import com.dr.jjsembako.akun.domain.repository.ISettingRepository
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

    @Binds
    abstract fun proviceHomeRepository(homeRepository: HomeRepository): IHomeRepository

}