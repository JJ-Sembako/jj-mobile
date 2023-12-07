package com.dr.jjsembako.core.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPreferencesModule {
    companion object{
        val PREFS_NAME = "auth_pref"
        val USERNAME_KEY = "username"
    }

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideToken(sharedPreferences: SharedPreferences): String {
        return sharedPreferences.getString(PREFS_NAME, "") ?: ""
    }

    @Singleton
    @Provides
    fun provideUsername(sharedPreferences: SharedPreferences): String {
        return sharedPreferences.getString(USERNAME_KEY, "") ?: "username"
    }
}