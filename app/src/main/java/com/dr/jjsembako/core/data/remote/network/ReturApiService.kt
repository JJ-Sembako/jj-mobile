package com.dr.jjsembako.core.data.remote.network

import com.dr.jjsembako.core.data.remote.response.retur.DeleteHandleDeleteReturResponse
import com.dr.jjsembako.core.data.remote.response.retur.PostHandleCreateReturResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface ReturApiService {

    @FormUrlEncoded
    @POST("retur")
    suspend fun handleCreateRetur(
        @Field("orderId") orderId: String,
        @Field("returedProductId") returedProductId: String,
        @Field("returnedProductId") returnedProductId: String,
        @Field("amountInUnit") amountInUnit: Int,
        @Field("selledPrice") selledPrice: Long
    ): PostHandleCreateReturResponse

    @DELETE("retur/{id}")
    suspend fun handleDeleteRetur(
        @Path("id") id: String
    ): DeleteHandleDeleteReturResponse
}