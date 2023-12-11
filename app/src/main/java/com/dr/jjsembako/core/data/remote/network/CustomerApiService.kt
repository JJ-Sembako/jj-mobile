package com.dr.jjsembako.core.data.remote.network

import com.dr.jjsembako.core.data.remote.response.customer.DeleteHandleDeleteCustomerResponse
import com.dr.jjsembako.core.data.remote.response.customer.GetFetchCustomersResponse
import com.dr.jjsembako.core.data.remote.response.customer.GetFetchDetailCustomerResponse
import com.dr.jjsembako.core.data.remote.response.customer.PostHandleCreateCustomerResponse
import com.dr.jjsembako.core.data.remote.response.customer.PutHandleUpdateCustomerResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CustomerApiService {

    @GET("customer")
    suspend fun fetchCustomers(
        @Query("search") search: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): GetFetchCustomersResponse

    @FormUrlEncoded
    @POST("customer")
    suspend fun handleCreateCustomer(
        @Field("name") name: String,
        @Field("shop_name") shopName: String,
        @Field("address") address: String,
        @Field("gmaps_link") gmapsLink: String,
        @Field("phone_number") phoneNumber: String
    ): PostHandleCreateCustomerResponse

    @GET("customer/{id}/detail")
    suspend fun fetchDetailCustomer(
        @Path("id") id: String
    ): GetFetchDetailCustomerResponse

    @FormUrlEncoded
    @PUT("customer/{id}")
    suspend fun handleUpdateCustomer(
        @Path("id") id: String,
        @Field("name") name: String,
        @Field("shop_name") shopName: String,
        @Field("address") address: String,
        @Field("gmaps_link") gmapsLink: String,
        @Field("phone_number") phoneNumber: String
    ): PutHandleUpdateCustomerResponse

    @DELETE("customer/{id}")
    suspend fun handleDeleteCustomer(
        @Path("id") id: String
    ): DeleteHandleDeleteCustomerResponse
}