package com.dr.jjsembako.core.data.remote.network

import com.dr.jjsembako.core.data.remote.response.category.GetFetchCategoriesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CategoryApiService {
    @GET("product")
    suspend fun fetchCategories(
        @Query("search") search: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): GetFetchCategoriesResponse
}