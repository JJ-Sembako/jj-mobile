package com.dr.jjsembako.pesanan.data

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.network.OrderApiService
import com.dr.jjsembako.core.data.remote.response.canceled.DeleteHandleDeleteCanceledResponse
import com.dr.jjsembako.core.data.remote.response.canceled.PostHandleCreateCanceledResponse
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class CanceledDataSource @Inject constructor(
    private val orderApiService: OrderApiService,
    private val gson: Gson
) {

    suspend fun handleCreateCanceled(
        orderId: String,
        productId: String,
        amountInUnit: Int
    ): Flow<Resource<out PostHandleCreateCanceledResponse>> = flow {
        try {
            val response = orderApiService.handleCreateCanceled(orderId, productId, amountInUnit)
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
                        gson.fromJson(errorResponse, PostHandleCreateCanceledResponse::class.java)
                    emit(Resource.Error(errorResponseObj.message, statusCode, null))
                } else {
                    emit(Resource.Error(errorMessage ?: "Unknown error", statusCode, null))
                }
            } else {
                emit(Resource.Error(e.message ?: "Unknown error", 400, null))
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun handleDeleteCanceled(id: String): Flow<Resource<out DeleteHandleDeleteCanceledResponse>> =
        flow {
            try {
                val response = orderApiService.handleDeleteCanceled(id)
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
                                DeleteHandleDeleteCanceledResponse::class.java
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