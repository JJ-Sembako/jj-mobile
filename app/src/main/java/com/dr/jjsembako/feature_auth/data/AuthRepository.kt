package com.dr.jjsembako.feature_auth.data

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataHandleLogin
import com.dr.jjsembako.core.data.remote.response.account.PostHandleLoginResponse
import com.dr.jjsembako.feature_auth.domain.repository.IAuthRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val authDataSource: AuthDataSource) :
    IAuthRepository {

    override suspend fun handleLogin(
        username: String,
        password: String
    ): Flow<Resource<out DataHandleLogin?>> = flow {
        emit(Resource.Loading())
        try {
            val response = authDataSource.handleLogin(username, password).first()

            when (response.status) {
                200 -> {
                    val data = response.data
                    emit(Resource.Success(data, response.message ?: "Unknown error", response.status))
                }
                else -> {
                    // Meneruskan pesan dan status code dari respon error
                    emit(Resource.Error(response.message ?: "Unknown error", response.status ?: 400))
                }
            }
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