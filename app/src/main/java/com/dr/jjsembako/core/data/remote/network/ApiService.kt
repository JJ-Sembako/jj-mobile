package com.dr.jjsembako.core.data.remote.network

import com.dr.jjsembako.core.data.remote.response.account.PostHandleLoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    /**********************
     * Account
     *********************/
    @FormUrlEncoded
    @POST("account/login")
    suspend fun handleLogin(
        @Field("username") email: String,
        @Field("password") password: String
    ): PostHandleLoginResponse
}