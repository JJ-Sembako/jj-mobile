package com.dr.jjsembako.feature_history.data

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.network.OrderApiService
import com.dr.jjsembako.core.data.remote.response.order.DeleteHandleDeleteOrderResponse
import com.dr.jjsembako.core.data.remote.response.order.DeleteHandleDeleteProductOrderResponse
import com.dr.jjsembako.core.data.remote.response.order.DetailOrder
import com.dr.jjsembako.core.data.remote.response.order.GetFetchOrderResponse
import com.dr.jjsembako.core.data.remote.response.order.PatchHandleUpdatePaymentStatusResponse
import com.dr.jjsembako.core.data.remote.response.order.PatchHandleUpdateProductOrderResponse
import com.dr.jjsembako.core.data.remote.response.order.PostHandleAddProductOrderResponse
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class HistoryDataSource @Inject constructor(
    private val orderApiService: OrderApiService,
    private val gson: Gson
) {

    suspend fun fetchOrder(id: String): Flow<Resource<out DetailOrder?>> = flow {
        try {
            val response = orderApiService.fetchOrder(id)
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
                        gson.fromJson(errorResponse, GetFetchOrderResponse::class.java)
                    emit(Resource.Error(errorResponseObj.message, statusCode, null))
                } else {
                    emit(Resource.Error(errorMessage ?: "Unknown error", statusCode, null))
                }
            } else {
                emit(Resource.Error(e.message ?: "Unknown error", 400, null))
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun handleAddProductOrder(
        id: String,
        productId: String,
        amountInUnit: Int,
        pricePerUnit: Long
    ): Flow<Resource<out PostHandleAddProductOrderResponse>> = flow {
        try {
            val response =
                orderApiService.handleAddProductOrder(id, productId, amountInUnit, pricePerUnit)
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
                        gson.fromJson(errorResponse, PostHandleAddProductOrderResponse::class.java)
                    emit(Resource.Error(errorResponseObj.message, statusCode, null))
                } else {
                    emit(Resource.Error(errorMessage ?: "Unknown error", statusCode, null))
                }
            } else {
                emit(Resource.Error(e.message ?: "Unknown error", 400, null))
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun handleUpdateProductOrder(
        id: String,
        productId: String,
        amountInUnit: Int,
        pricePerUnit: Long
    ): Flow<Resource<out PatchHandleUpdateProductOrderResponse>> = flow {
        try {
            val response =
                orderApiService.handleUpdateProductOrder(id, productId, amountInUnit, pricePerUnit)
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
                            PatchHandleUpdateProductOrderResponse::class.java
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

    suspend fun handleUpdatePaymentStatus(id: String): Flow<Resource<out PatchHandleUpdatePaymentStatusResponse>> =
        flow {
            try {
                val response =
                    orderApiService.handleUpdatePaymentStatus(id)
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
                                PatchHandleUpdatePaymentStatusResponse::class.java
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

    suspend fun handleDeleteProductOrder(
        id: String,
        productId: String
    ): Flow<Resource<out DeleteHandleDeleteProductOrderResponse>> = flow {
        try {
            val response =
                orderApiService.handleDeleteProductOrder(id, productId)
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
                            DeleteHandleDeleteProductOrderResponse::class.java
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

    suspend fun handleDeleteOrder(id: String): Flow<Resource<out DeleteHandleDeleteOrderResponse>> =
        flow {
            try {
                val response =
                    orderApiService.handleDeleteOrder(id)
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
                                DeleteHandleDeleteOrderResponse::class.java
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