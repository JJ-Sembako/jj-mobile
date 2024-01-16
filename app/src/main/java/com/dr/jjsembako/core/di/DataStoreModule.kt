package com.dr.jjsembako.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
//    companion object {
//        val PREFS_NAME = "order"
//        val CUSTOMER_KEY = "customer"
//        val PAYMENT_KEY = "payment"
//        val PRODUCT_KEY = "product"
//    }
//
//    @Singleton
//    @Provides
//    fun provideDataStore(@ApplicationContext context: Context) : DataStore<Preferences> {
//        context.createDataStore(name = "order_data")
//    }
}