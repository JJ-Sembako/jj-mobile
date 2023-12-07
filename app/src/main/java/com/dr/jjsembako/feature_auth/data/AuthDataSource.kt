package com.dr.jjsembako.feature_auth.data

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.network.AccountApiService
import com.dr.jjsembako.core.data.remote.response.account.DataHandleLogin
import com.dr.jjsembako.core.data.remote.response.account.PostHandleLoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

class AuthDataSource @Inject constructor(private val accountApiService: AccountApiService) {
//    }
    suspend fun handleLogin(
        username: String,
        password: String
    ): Flow<Resource<out DataHandleLogin?>> = flow {
        try {
            val response = accountApiService.handleLogin(username, password)
            emit(Resource.Success(response.data, response.message, response.statusCode))
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorResponse = e.response()?.errorBody()?.string()
                val statusCode = e.code()
                val errorMessage = e.message()

                if (errorResponse != null) {
                    val errorResponseObj = Gson().fromJson(errorResponse, PostHandleLoginResponse::class.java)
                    emit(Resource.Error(errorResponseObj.message, statusCode, null))
                } else {
                    emit(Resource.Error(errorMessage ?: "Unknown error", statusCode, null))
                }
            } else {
                emit(Resource.Error(e.message ?: "Unknown error", 400, null))
            }
        }
    }.flowOn(Dispatchers.IO)
}