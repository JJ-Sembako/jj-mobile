package com.dr.jjsembako.core.data.remote.network

import com.dr.jjsembako.core.data.remote.response.performance.GetFetchOmzetMonthlyResponse
import com.dr.jjsembako.core.data.remote.response.performance.GetFetchOmzetResponse
import com.dr.jjsembako.core.data.remote.response.performance.GetFetchSelledProductResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PerformanceApiService {

    @GET("performance/omzet")
    suspend fun fetchOmzet(
        @Query("me") me: Int = 1
    ): GetFetchOmzetResponse

    @GET("performance/omzet/monthly")
    suspend fun fetchOmzetMonthly(
        @Query("month") month: Int,
        @Query("year") year: Int,
        @Query("me") me: Int = 1
    ): GetFetchOmzetMonthlyResponse

    @GET("performance/selled-product/monthly")
    suspend fun fetchSelledProductMonthly(
        @Query("month") month: Int,
        @Query("year") year: Int,
        @Query("me") me: Int = 1
    ): GetFetchSelledProductResponse
}