package com.dr.jjsembako.core.data.remote.network

import com.dr.jjsembako.core.data.model.OrderRequest
import com.dr.jjsembako.core.data.remote.response.order.DeleteHandleDeleteOrderResponse
import com.dr.jjsembako.core.data.remote.response.order.DeleteHandleDeleteProductOrderResponse
import com.dr.jjsembako.core.data.remote.response.order.GetFetchOrderResponse
import com.dr.jjsembako.core.data.remote.response.order.GetFetchOrdersResponse
import com.dr.jjsembako.core.data.remote.response.order.PatchHandleUpdatePaymentStatusResponse
import com.dr.jjsembako.core.data.remote.response.order.PatchHandleUpdateProductOrderResponse
import com.dr.jjsembako.core.data.remote.response.order.PostHandleAddProductOrderResponse
import com.dr.jjsembako.core.data.remote.response.order.PostHandleCreateOrderResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderApiService {

    @GET("order")
    suspend fun fetchOrders(
        @Query("search") search: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("minDate") minDate: String? = null,
        @Query("maxDate") maxDate: String? = null,
        @Query("me") me: Int? = null,
        @Query("customerId") customerId: String? = null
    ): GetFetchOrdersResponse

    @GET("order/{id}")
    suspend fun fetchOrder(
        @Path("id") id: String
    ): GetFetchOrderResponse

    @POST("order")
    suspend fun handleCreateOrder(
        @Body orderRequest: OrderRequest
    ): PostHandleCreateOrderResponse

    @FormUrlEncoded
    @POST("order/{id}")
    suspend fun handleAddProductOrder(
        @Path("id") id: String,
        @Field("productId") productId: String,
        @Field("amountInUnit") amountInUnit: Int,
        @Field("pricePerUnit") pricePerUnit: Long
    ): PostHandleAddProductOrderResponse

    @FormUrlEncoded
    @PATCH("order/{id}/product/{productId}")
    suspend fun handleUpdateProductOrder(
        @Path("id") id: String,
        @Path("productId") productId: String,
        @Field("amountInUnit") amountInUnit: Int,
        @Field("pricePerUnit") pricePerUnit: Long
    ): PatchHandleUpdateProductOrderResponse

    @PATCH("order/{id}/payment")
    suspend fun handleUpdatePaymentStatus(
        @Path("id") id: String
    ): PatchHandleUpdatePaymentStatusResponse

    @DELETE("order/{id}/product/{productId}")
    suspend fun handleDeleteProductOrder(
        @Path("id") id: String,
        @Path("productId") productId: String
    ): DeleteHandleDeleteProductOrderResponse

    @DELETE("order/{id}")
    suspend fun handleDeleteOrder(
        @Path("id") id: String
    ): DeleteHandleDeleteOrderResponse
}