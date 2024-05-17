package com.dr.jjsembako.akun.data

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.network.AccountApiService
import com.dr.jjsembako.core.data.remote.response.account.DataAccountRecovery
import com.dr.jjsembako.core.data.remote.response.account.DataActivateAccountRecovery
import com.dr.jjsembako.core.data.remote.response.account.DataRecoveryQuestion
import com.dr.jjsembako.core.data.remote.response.account.GetFetchAccountRecoveryQuestionsResponse
import com.dr.jjsembako.core.data.remote.response.account.GetFetchAccountRecoveryResponse
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleDeactivateAccountRecoveryResponse
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleUpdateSelfPasswordResponse
import com.dr.jjsembako.core.data.remote.response.account.PostHandleActivateAccountRecoveryResponse
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class SettingDataSource @Inject constructor(
    private val accountApiService: AccountApiService,
    private val gson: Gson
) {

    suspend fun handleUpdateSelfPassword(
        oldPassword: String,
        newPassword: String,
        confNewPassword: String
    ): Flow<Resource<out PatchHandleUpdateSelfPasswordResponse>> = flow {
        try {
            val response = accountApiService.handleUpdateSelfPassword(
                oldPassword,
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
                            PatchHandleUpdateSelfPasswordResponse::class.java
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

    suspend fun fetchAccountRecoveryQuestions(): Flow<Resource<out List<DataRecoveryQuestion?>>> =
        flow {
            try {
                val response = accountApiService.fetchAccountRecoveryQuestions()
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
                                GetFetchAccountRecoveryQuestionsResponse::class.java
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

    suspend fun fetchAccountRecovery(): Flow<Resource<out DataAccountRecovery?>> = flow {
        try {
            val response = accountApiService.fetchAccountRecovery()
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
                            GetFetchAccountRecoveryResponse::class.java
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

    suspend fun handleActivateAccountRecovery(
        idQuestion: String,
        answer: String
    ): Flow<Resource<out DataActivateAccountRecovery?>> = flow {
        try {
            val response = accountApiService.handleActivateAccountRecovery(idQuestion, answer)
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
                            PostHandleActivateAccountRecoveryResponse::class.java
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

    suspend fun handleDeactivateAccountRecovery(): Flow<Resource<out PatchHandleDeactivateAccountRecoveryResponse>> =
        flow {
            try {
                val response = accountApiService.handleDeactivateAccountRecovery()
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
                                PatchHandleDeactivateAccountRecoveryResponse::class.java
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