package com.dr.jjsembako.feature_auth.data

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.network.AccountApiService
import com.dr.jjsembako.core.data.remote.response.account.DataAccountRecoveryQuestion
import com.dr.jjsembako.core.data.remote.response.account.DataCheckAccountRecoveryActivation
import com.dr.jjsembako.core.data.remote.response.account.DataCheckAccountRecoveryAnswer
import com.dr.jjsembako.core.data.remote.response.account.DataHandleLogin
import com.dr.jjsembako.core.data.remote.response.account.GetCheckAccountRecoveryActivationResponse
import com.dr.jjsembako.core.data.remote.response.account.GetFetchAccountRecoveryQuestionByUsernameResponse
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleUpdatePasswordFromRecoveryResponse
import com.dr.jjsembako.core.data.remote.response.account.PostCheckAccountRecoveryAnswerResponse
import com.dr.jjsembako.core.data.remote.response.account.PostHandleLoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val accountApiService: AccountApiService,
    private val gson: Gson
) {

    suspend fun handleLogin(
        username: String,
        password: String
    ): Flow<Resource<out DataHandleLogin?>> = flow {
        try {
            val response = accountApiService.handleLogin(username, password)
            emit(Resource.Success(response.data, response.message, response.statusCode))
        } catch (e: CancellationException) {
            // Do nothing, the flow is cancelled
        } catch (e: IOException) {
            // Handle IOException (connection issues, timeout, etc.)
            emit(Resource.Error("Masalah jaringan koneksi internet", 408, null))
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorResponse = e.response()?.errorBody()?.string()
                val statusCode = e.code()
                val errorMessage = e.message()

                if (errorResponse != null) {
                    val errorResponseObj =
                        gson.fromJson(errorResponse, PostHandleLoginResponse::class.java)
                    emit(Resource.Error(errorResponseObj.message, statusCode, null))
                } else {
                    emit(Resource.Error(errorMessage ?: "Unknown error", statusCode, null))
                }
            } else {
                emit(Resource.Error(e.message ?: "Unknown error", 400, null))
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun checkAccountRecoveryActivation(username: String): Flow<Resource<out DataCheckAccountRecoveryActivation?>> =
        flow {
            try {
                val response = accountApiService.checkAccountRecoveryActivation(username)
                emit(Resource.Success(response.data, response.message, response.statusCode))
            } catch (e: CancellationException) {
                // Do nothing, the flow is cancelled
            } catch (e: IOException) {
                // Handle IOException (connection issues, timeout, etc.)
                emit(Resource.Error("Masalah jaringan koneksi internet", 408, null))
            } catch (e: Exception) {
                if (e is HttpException) {
                    val errorResponse = e.response()?.errorBody()?.string()
                    val statusCode = e.code()
                    val errorMessage = e.message()

                    if (errorResponse != null) {
                        val errorResponseObj =
                            gson.fromJson(
                                errorResponse,
                                GetCheckAccountRecoveryActivationResponse::class.java
                            )
                        emit(Resource.Error(errorResponseObj.message, statusCode, null))
                    } else {
                        emit(Resource.Error(errorMessage ?: "Unknown error", statusCode, null))
                    }
                } else {
                    emit(Resource.Error(e.message ?: "Unknown error", 400, null))
                }
            }
        }.flowOn(Dispatchers.IO)

    suspend fun fetchAccountRecoveryQuestionByUsername(username: String): Flow<Resource<out DataAccountRecoveryQuestion?>> =
        flow {
            try {
                val response = accountApiService.fetchAccountRecoveryQuestionByUsername(username)
                emit(Resource.Success(response.data, response.message, response.statusCode))
            } catch (e: CancellationException) {
                // Do nothing, the flow is cancelled
            } catch (e: IOException) {
                // Handle IOException (connection issues, timeout, etc.)
                emit(Resource.Error("Masalah jaringan koneksi internet", 408, null))
            } catch (e: Exception) {
                if (e is HttpException) {
                    val errorResponse = e.response()?.errorBody()?.string()
                    val statusCode = e.code()
                    val errorMessage = e.message()

                    if (errorResponse != null) {
                        val errorResponseObj =
                            gson.fromJson(
                                errorResponse,
                                GetFetchAccountRecoveryQuestionByUsernameResponse::class.java
                            )
                        emit(Resource.Error(errorResponseObj.message, statusCode, null))
                    } else {
                        emit(Resource.Error(errorMessage ?: "Unknown error", statusCode, null))
                    }
                } else {
                    emit(Resource.Error(e.message ?: "Unknown error", 400, null))
                }
            }
        }.flowOn(Dispatchers.IO)

    suspend fun checkAccountRecoveryAnswer(
        username: String,
        answer: String
    ): Flow<Resource<out DataCheckAccountRecoveryAnswer?>> = flow {
        try {
            val response = accountApiService.checkAccountRecoveryAnswer(username, answer)
            emit(Resource.Success(response.data, response.message, response.statusCode))
        } catch (e: CancellationException) {
            // Do nothing, the flow is cancelled
        } catch (e: IOException) {
            // Handle IOException (connection issues, timeout, etc.)
            emit(Resource.Error("Masalah jaringan koneksi internet", 408, null))
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorResponse = e.response()?.errorBody()?.string()
                val statusCode = e.code()
                val errorMessage = e.message()

                if (errorResponse != null) {
                    val errorResponseObj =
                        gson.fromJson(
                            errorResponse,
                            PostCheckAccountRecoveryAnswerResponse::class.java
                        )
                    emit(Resource.Error(errorResponseObj.message, statusCode, null))
                } else {
                    emit(Resource.Error(errorMessage ?: "Unknown error", statusCode, null))
                }
            } else {
                emit(Resource.Error(e.message ?: "Unknown error", 400, null))
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun handleUpdatePasswordFromRecovery(
        username: String,
        newPassword: String,
        confNewPassword: String
    ): Flow<Resource<out PatchHandleUpdatePasswordFromRecoveryResponse>> = flow {
        try {
            val response = accountApiService.handleUpdatePasswordFromRecovery(
                username,
                newPassword,
                confNewPassword
            )
            emit(Resource.Success(null, response.message, response.statusCode))
        } catch (e: CancellationException) {
            // Do nothing, the flow is cancelled
        } catch (e: IOException) {
            // Handle IOException (connection issues, timeout, etc.)
            emit(Resource.Error("Masalah jaringan koneksi internet", 408, null))
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorResponse = e.response()?.errorBody()?.string()
                val statusCode = e.code()
                val errorMessage = e.message()

                if (errorResponse != null) {
                    val errorResponseObj =
                        gson.fromJson(
                            errorResponse,
                            PatchHandleUpdatePasswordFromRecoveryResponse::class.java
                        )
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