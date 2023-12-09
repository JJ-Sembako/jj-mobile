package com.dr.jjsembako.core.data.remote.network

import com.dr.jjsembako.core.data.remote.response.account.GetCheckAccountRecoveryActivationResponse
import com.dr.jjsembako.core.data.remote.response.account.GetFetchAccountRecoveryQuestionByUsernameResponse
import com.dr.jjsembako.core.data.remote.response.account.GetFetchAccountRecoveryQuestionsResponse
import com.dr.jjsembako.core.data.remote.response.account.GetFetchAccountRecoveryResponse
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleDeactivateAccountRecoveryResponse
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleUpdatePasswordFromRecoveryResponse
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleUpdateSelfPasswordResponse
import com.dr.jjsembako.core.data.remote.response.account.PostCheckAccountRecoveryAnswerResponse
import com.dr.jjsembako.core.data.remote.response.account.PostHandleActivateAccountRecoveryResponse
import com.dr.jjsembako.core.data.remote.response.account.PostHandleLoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("account/recovery/activate/{username}")
    suspend fun checkAccountRecoveryActivation(
        @Path("username") username: String
    ): GetCheckAccountRecoveryActivationResponse

    @GET("account/recovery/question/{username}")
    suspend fun fetchAccountRecoveryQuestionByUsername(
        @Path("username") username: String
    ): GetFetchAccountRecoveryQuestionByUsernameResponse

    @FormUrlEncoded
    @POST("account/recovery/answer/valid/{username}")
    suspend fun checkAccountRecoveryAnswer(
        @Path("username") username: String,
        @Field("answer") answer: String
    ): PostCheckAccountRecoveryAnswerResponse

    @FormUrlEncoded
    @PATCH("account/password/recovery/{username}")
    suspend fun handleUpdatePasswordFromRecovery(
        @Path("username") username: String,
        @Field("newPassword") newPassword: String,
        @Field("confirmationPassword") confirmationPassword: String
    ): PatchHandleUpdatePasswordFromRecoveryResponse
}