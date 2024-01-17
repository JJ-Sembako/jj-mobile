package com.dr.jjsembako.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.dr.jjsembako.ProductOrderList
import com.dr.jjsembako.core.utils.ProductOrderStoreSerializer
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
    private const val DATA_STORE_FILE_NAME = "ordered_product_prefs.pb"
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
    fun provideProtoDataStore(@ApplicationContext appContext: Context): DataStore<ProductOrderList> {
        return DataStoreFactory.create(
            serializer = ProductOrderStoreSerializer(),
            produceFile = { appContext.dataStoreFile(DATA_STORE_FILE_NAME) },
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
    fun providePaymentPreference(dataStore: DataStore<Preferences>): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.PAYMENT] ?: false
        }
    }

    @Singleton
    @Provides
    fun provideProductsList(dataStore: DataStore<ProductOrderList>): Flow<ProductOrderList> {
        return dataStore.data
    }

    private object PreferencesKeys {
        val ID_CUSTOMER = stringPreferencesKey("id_customer")
        val PAYMENT = booleanPreferencesKey("payment")
    }
}