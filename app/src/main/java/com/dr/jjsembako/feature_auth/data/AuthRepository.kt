package com.dr.jjsembako.feature_auth.data

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataAccountRecoveryQuestion
import com.dr.jjsembako.core.data.remote.response.account.DataCheckAccountRecoveryActivation
import com.dr.jjsembako.core.data.remote.response.account.DataCheckAccountRecoveryAnswer
import com.dr.jjsembako.core.data.remote.response.account.DataHandleLogin
import com.dr.jjsembako.core.data.remote.response.account.GetCheckAccountRecoveryActivationResponse
import com.dr.jjsembako.core.data.remote.response.account.GetFetchAccountRecoveryQuestionByUsernameResponse
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleUpdatePasswordFromRecoveryResponse
import com.dr.jjsembako.core.data.remote.response.account.PostCheckAccountRecoveryAnswerResponse
import com.dr.jjsembako.core.data.remote.response.account.PostHandleLoginResponse
import com.dr.jjsembako.feature_auth.domain.repository.IAuthRepository
import com.dr.jjsembako.feature_auth.domain.repository.IForgetPasswordRepository
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
class AuthRepository @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val gson: Gson
) : IAuthRepository, IForgetPasswordRepository {

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
                    emit(
                        Resource.Success(
                            data,
                            response.message ?: "Unknown error",
                            response.status
                        )
                    )
                }

                else -> {
                    // Meneruskan pesan dan status code dari respon error
                    emit(
                        Resource.Error(
                            response.message ?: "Unknown error",
                            response.status ?: 400
                        )
                    )
                }
            }
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

    override suspend fun checkAccountRecoveryActivation(username: String): Flow<Resource<out DataCheckAccountRecoveryActivation?>> =
        flow {
            emit(Resource.Loading())
            try {
                val response = authDataSource.checkAccountRecoveryActivation(username).first()

                when (response.status) {
                    200 -> {
                        val data = response.data
                        emit(
                            Resource.Success(
                                data,
                                response.message ?: "Unknown error",
                                response.status
                            )
                        )
                    }

                    else -> {
                        // Meneruskan pesan dan status code dari respon error
                        emit(
                            Resource.Error(
                                response.message ?: "Unknown error",
                                response.status ?: 400
                            )
                        )
                    }
                }
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

    override suspend fun fetchAccountRecoveryQuestionByUsername(username: String): Flow<Resource<out DataAccountRecoveryQuestion?>> =
        flow {
            emit(Resource.Loading())
            try {
                val response =
                    authDataSource.fetchAccountRecoveryQuestionByUsername(username).first()

                when (response.status) {
                    200 -> {
                        val data = response.data
                        emit(
                            Resource.Success(
                                data,
                                response.message ?: "Unknown error",
                                response.status
                            )
                        )
                    }

                    else -> {
                        // Meneruskan pesan dan status code dari respon error
                        emit(
                            Resource.Error(
                                response.message ?: "Unknown error",
                                response.status ?: 400
                            )
                        )
                    }
                }
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

    override suspend fun checkAccountRecoveryAnswer(
        username: String,
        answer: String
    ): Flow<Resource<out DataCheckAccountRecoveryAnswer?>> = flow {
        emit(Resource.Loading())
        try {
            val response = authDataSource.checkAccountRecoveryAnswer(username, answer).first()

            when (response.status) {
                200 -> {
                    val data = response.data
                    emit(
                        Resource.Success(
                            data,
                            response.message ?: "Unknown error",
                            response.status
                        )
                    )
                }

                else -> {
                    // Meneruskan pesan dan status code dari respon error
                    emit(
                        Resource.Error(
                            response.message ?: "Unknown error",
                            response.status ?: 400
                        )
                    )
                }
            }
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

    override suspend fun handleUpdatePasswordFromRecovery(
        username: String,
        newPassword: String,
        confNewPassword: String
    ): Flow<Resource<out PatchHandleUpdatePasswordFromRecoveryResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = authDataSource.handleUpdatePasswordFromRecovery(
                username,
                newPassword,
                confNewPassword
            ).first()

            when (response.status) {
                200 -> {
                    val data = response.data
                    emit(
                        Resource.Success(
                            data,
                            response.message ?: "Unknown error",
                            response.status
                        )
                    )
                }

                else -> {
                    // Meneruskan pesan dan status code dari respon error
                    emit(
                        Resource.Error(
                            response.message ?: "Unknown error",
                            response.status ?: 400
                        )
                    )
                }
            }
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