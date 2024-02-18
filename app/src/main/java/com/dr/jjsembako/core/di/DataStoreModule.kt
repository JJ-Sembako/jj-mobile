package com.dr.jjsembako.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.dr.jjsembako.CanceledStore
import com.dr.jjsembako.ProductOrderList
import com.dr.jjsembako.ReturStore
import com.dr.jjsembako.SubstituteStore
import com.dr.jjsembako.core.data.model.PreferencesKeys
import com.dr.jjsembako.core.utils.proto.CanceledStoreSerializer
import com.dr.jjsembako.core.utils.proto.ProductOrderStoreSerializer
import com.dr.jjsembako.core.utils.proto.ReturStoreSerializer
import com.dr.jjsembako.core.utils.proto.SubstituteStoreSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private const val ORDER_PREFERENCES = "order_preferences"
    private const val DATA_STORE_FILE_CREATE_ORDER = "ordered_product_prefs.pb"
    private const val DATA_STORE_FILE_CANCELED = "canceled_prefs.pb"
    private const val DATA_STORE_FILE_RETUR = "retur_prefs.pb"
    private const val DATA_STORE_FILE_SUBSTITUTE_RETUR = "substitute_retur_prefs.pb"

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(ORDER_PREFERENCES) }
        )
    }

    @Singleton
    @Provides
    fun provideProtoDataStoreCreateOrder(@ApplicationContext appContext: Context): DataStore<ProductOrderList> {
        return DataStoreFactory.create(
            serializer = ProductOrderStoreSerializer(),
            produceFile = { appContext.dataStoreFile(DATA_STORE_FILE_CREATE_ORDER) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Singleton
    @Provides
    fun provideProtoDataStoreCanceled(@ApplicationContext appContext: Context): DataStore<CanceledStore> {
        return DataStoreFactory.create(
            serializer = CanceledStoreSerializer(),
            produceFile = { appContext.dataStoreFile(DATA_STORE_FILE_CANCELED) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Singleton
    @Provides
    fun provideProtoDataStoreRetur(@ApplicationContext appContext: Context): DataStore<ReturStore> {
        return DataStoreFactory.create(
            serializer = ReturStoreSerializer(),
            produceFile = { appContext.dataStoreFile(DATA_STORE_FILE_RETUR) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Singleton
    @Provides
    fun provideProtoDataStoreSubstitute(@ApplicationContext appContext: Context): DataStore<SubstituteStore> {
        return DataStoreFactory.create(
            serializer = SubstituteStoreSerializer(),
            produceFile = { appContext.dataStoreFile(DATA_STORE_FILE_SUBSTITUTE_RETUR) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Singleton
    @Provides
    fun provideIdCustomerPreference(dataStore: DataStore<Preferences>): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.ID_CUSTOMER] ?: ""
        }
    }

    @Singleton
    @Provides
    fun providePaymentPreference(dataStore: DataStore<Preferences>): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.PAYMENT] ?: "PENDING"
        }
    }

    @Singleton
    @Provides
    fun provideProductsList(dataStore: DataStore<ProductOrderList>): Flow<ProductOrderList> {
        return dataStore.data
    }

    @Singleton
    @Provides
    fun provideCanceledData(dataStore: DataStore<CanceledStore>): Flow<CanceledStore> {
        return dataStore.data
    }

    @Singleton
    @Provides
    fun provideReturData(dataStore: DataStore<ReturStore>): Flow<ReturStore> {
        return dataStore.data
    }

    @Singleton
    @Provides
    fun provideSubstituteData(dataStore: DataStore<SubstituteStore>): Flow<SubstituteStore> {
        return dataStore.data
    }
}