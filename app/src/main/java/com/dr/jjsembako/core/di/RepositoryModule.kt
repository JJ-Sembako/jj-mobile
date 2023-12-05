package com.dr.jjsembako.core.di

import com.dr.jjsembako.core.data.JJSRepository
import com.dr.jjsembako.core.domain.repository.IAccountRepository
import com.dr.jjsembako.core.domain.repository.ICustomerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideRepositoryAccount(jjsRepository: JJSRepository): IAccountRepository

    @Binds
    abstract fun provideRepositoryCustomer(jjsRepository: JJSRepository): ICustomerRepository
}