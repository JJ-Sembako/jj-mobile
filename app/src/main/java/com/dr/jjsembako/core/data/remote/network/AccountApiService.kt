package com.dr.jjsembako.core.data.remote.network

import com.dr.jjsembako.core.data.remote.response.account.PatchHandleUpdateSelfPasswordResponse
import com.dr.jjsembako.core.data.remote.response.account.PostHandleLoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AccountApiService {

    @FormUrlEncoded
    @POST("account/login")
    suspend fun handleLogin(
        @Field("username") email: String,
        @Field("password") password: String
    ): PostHandleLoginResponse

    @FormUrlEncoded
    @PATCH("account/password")
    suspend fun handleUpdateSelfPassword(
        @Field("currentPassword") currentPassword: String,
        @Field("newPassword") newPassword: String,
        @Field("confirmationPassword") confirmationPassword: String
    ): PatchHandleUpdateSelfPasswordResponse
}