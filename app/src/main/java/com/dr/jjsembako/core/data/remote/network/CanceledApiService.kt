package com.dr.jjsembako.core.data.remote.network

import com.dr.jjsembako.core.data.remote.response.canceled.DeleteHandleDeleteCanceledResponse
import com.dr.jjsembako.core.data.remote.response.canceled.PostHandleCreateCanceledResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface CanceledApiService {

    @FormUrlEncoded
    @POST("canceled")
    suspend fun handleCreateCanceled(
        @Field("orderId") orderId: String,
        @Field("productId") productId: String,
        @Field("amountInUnit") amountInUnit: Int
    ): PostHandleCreateCanceledResponse

    @DELETE("canceled/{id}")
    suspend fun handleDeleteCanceled(
        @Path("id") id: String
    ): DeleteHandleDeleteCanceledResponse
}