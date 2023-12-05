package com.dr.jjsembako.feature_auth.data

import com.dr.jjsembako.core.common.ApiResponse
import com.dr.jjsembako.core.data.remote.network.AccountApiService
import com.dr.jjsembako.core.data.remote.response.account.PostHandleLoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthDataSource @Inject constructor(private val accountApiService: AccountApiService) {

    suspend fun handleLogin(
        username: String,
        password: String
    ): Flow<ApiResponse<PostHandleLoginResponse>> = flow {
        val response = accountApiService.handleLogin(username, password)
        if (response.statusCode == 200) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }
}