package com.dr.jjsembako.core.data.remote.network

import com.dr.jjsembako.core.data.remote.response.product.GetFetchProductsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApiService {
    @GET("product")
    suspend fun fetchProducts(
        @Query("search") search: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("minStock") minStock: Int? = null,
        @Query("category") category: String? = null
    ): GetFetchProductsResponse
}