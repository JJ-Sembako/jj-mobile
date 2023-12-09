package com.dr.jjsembako.core.data.remote.network

import com.dr.jjsembako.core.data.remote.response.account.GetFetchAccountRecoveryQuestionsResponse
import com.dr.jjsembako.core.data.remote.response.account.GetFetchAccountRecoveryResponse
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleDeactivateAccountRecoveryResponse
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleUpdateSelfPasswordResponse
import com.dr.jjsembako.core.data.remote.response.account.PostHandleActivateAccountRecoveryResponse
import com.dr.jjsembako.core.data.remote.response.account.PostHandleLoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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

    @GET("account/recovery/question")
    suspend fun fetchAccountRecoveryQuestions(): GetFetchAccountRecoveryQuestionsResponse

    @GET("account/recovery")
    suspend fun fetchAccountRecovery(): GetFetchAccountRecoveryResponse

    @FormUrlEncoded
    @POST("account/recovery/activate")
    suspend fun handleActivateAccountRecovery(
        @Field("idQuestion") idQuestion: String,
        @Field("answer") answer: String
    ): PostHandleActivateAccountRecoveryResponse

    @PATCH("account/recovery/deactivate")
    suspend fun handleDeactivateAccountRecovery(): PatchHandleDeactivateAccountRecoveryResponse
}