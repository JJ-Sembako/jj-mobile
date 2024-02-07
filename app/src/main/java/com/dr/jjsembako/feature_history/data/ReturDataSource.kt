package com.dr.jjsembako.feature_history.data

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.network.ReturApiService
import com.dr.jjsembako.core.data.remote.response.retur.DeleteHandleDeleteReturResponse
import com.dr.jjsembako.core.data.remote.response.retur.PostHandleCreateReturResponse
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class ReturDataSource @Inject constructor(
    private val returApiService: ReturApiService,
    private val gson: Gson
) {

    suspend fun handleCreateRetur(
        orderId: String,
        returedProductId: String,
        returnedProductId: String,
        amountInUnit: Int,
        selledPrice: Long
    ): Flow<Resource<out PostHandleCreateReturResponse>> = flow {
        try {
            val response = returApiService.handleCreateRetur(
                orderId,
                returedProductId,
                returnedProductId,
                amountInUnit,
                selledPrice
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
                        gson.fromJson(errorResponse, PostHandleCreateReturResponse::class.java)
                    emit(Resource.Error(errorResponseObj.message, statusCode, null))
                } else {
                    emit(Resource.Error(errorMessage ?: "Unknown error", statusCode, null))
                }
            } else {
                emit(Resource.Error(e.message ?: "Unknown error", 400, null))
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun handleDeleteRetur(id: String): Flow<Resource<out DeleteHandleDeleteReturResponse>> =
        flow {
            try {
                val response = returApiService.handleDeleteRetur(id)
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
                                DeleteHandleDeleteReturResponse::class.java
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