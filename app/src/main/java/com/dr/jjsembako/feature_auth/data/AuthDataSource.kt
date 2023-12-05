package com.dr.jjsembako.feature_auth.data

import android.accounts.Account
import android.util.Log
import com.dr.jjsembako.core.common.ApiResponse
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.network.ApiService
import com.dr.jjsembako.core.data.remote.response.account.PostHandleLoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthDataSource @Inject constructor(private val apiService: ApiService) {
//    suspend fun handleLogin(
//        username: String,
//        password: String
//    ): Flow<ApiResponse<PostHandleLoginResponse>> {
//        return flow {
//            try {
//                val response = apiService.handleLogin(username, password)
//                if (response.data != null) {
//                    emit(ApiResponse.Success(response))
//                } else {
//                    emit(ApiResponse.Empty)
//                }
//            } catch (e: Exception) {
//                emit(ApiResponse.Error(e.toString()))
//                Log.e("AuthDataSource", e.toString())
//            }
//        }.flowOn(Dispatchers.IO)
//    }

//    suspend fun handleLogin(
//        username: String,
//        password: String
//    ): Flow<Resource<Account>> {
//        return flow {
//            emit(Resource.Loading())
//            try {
//                val response = apiService.handleLogin(username, password)
//                if (response.statusCode == 200) {
//                    val account = Account(
//                        response.data?.username ?: "",
//                        response.data?.role ?: ""
//                    )
//                    emit(Resource.Success(account))
//                } else {
//                    emit(Resource.Error(response.message))
//                }
//            } catch (e: Exception) {
//                emit(Resource.Error(e.message ?: "Error occurred"))
//            }
//        }.flowOn(Dispatchers.IO)
//    }
}