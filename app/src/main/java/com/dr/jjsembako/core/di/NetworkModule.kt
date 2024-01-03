package com.dr.jjsembako.core.di

import android.content.SharedPreferences
import com.dr.jjsembako.BuildConfig
import com.dr.jjsembako.core.data.remote.network.AccountApiService
import com.dr.jjsembako.core.data.remote.network.CustomerApiService
import com.dr.jjsembako.core.data.remote.network.ProductApiService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {


    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideGson() = gson

    @Provides
    @Singleton
    @Named("primary")
    fun provideOkHttpClient(sharedPreferences: SharedPreferences): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = sharedPreferences.getString("token", "")

                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()

                chain.proceed(newRequest)
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("webSocket")
    fun provideOkhttpWebSocket(sharedPreferences: SharedPreferences): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = sharedPreferences.getString("token", "")

                val newRequest = chain.request().newBuilder()
                    .addHeader("Upgrade", "websocket")
                    .addHeader("Connection", "Upgrade")
                    .addHeader("Authorization", "Bearer $token")
                    .build()

                chain.proceed(newRequest)
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(@Named("primary") client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    fun provideAccountService(retrofit: Retrofit): AccountApiService {
        return retrofit.create(AccountApiService::class.java)
    }

    @Provides
    fun provideCustomerService(retrofit: Retrofit): CustomerApiService {
        return retrofit.create(CustomerApiService::class.java)
    }

    @Provides
    fun provideProductService(retrofit: Retrofit): ProductApiService {
        return retrofit.create(ProductApiService::class.java)
    }

    @Provides
    @Named("wsProduct")
    fun provideWebSocketProductRequest() = Request.Builder()
        .url(BuildConfig.WS_URL + "product")
        .build()
}