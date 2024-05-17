package com.dr.jjsembako.core.home.data

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.network.OrderApiService
import com.dr.jjsembako.core.data.remote.network.PerformanceApiService
import com.dr.jjsembako.core.data.remote.response.order.GetFetchOrdersResponse
import com.dr.jjsembako.pesanan.domain.model.Order
import com.dr.jjsembako.core.data.remote.response.performance.GetFetchOmzetMonthlyResponse
import com.dr.jjsembako.core.data.remote.response.performance.GetFetchSelledProductResponse
import com.dr.jjsembako.performa.domain.model.Omzet
import com.dr.jjsembako.performa.domain.model.SelledProduct
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class HomeDataSource @Inject constructor(
    private val performanceApiService: PerformanceApiService,
    private val orderApiService: OrderApiService,
    private val gson: Gson
) {

    suspend fun fetchOmzetMonthly(month: Int, year: Int): Flow<Resource<out Omzet?>> = flow {
        try {
            val response = performanceApiService.fetchOmzetMonthly(month, year)
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
                        gson.fromJson(errorResponse, GetFetchOmzetMonthlyResponse::class.java)
                    emit(Resource.Error(errorResponseObj.message, statusCode, null))
                } else {
                    emit(Resource.Error(errorMessage ?: "Unknown error", statusCode, null))
                }
            } else {
                emit(Resource.Error(e.message ?: "Unknown error", 400, null))
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun fetchSelledProductMonthly(
        month: Int,
        year: Int
    ): Flow<Resource<out List<SelledProduct?>?>> = flow {
        try {
            val response = performanceApiService.fetchSelledProductMonthly(month, year)
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
                        gson.fromJson(errorResponse, GetFetchSelledProductResponse::class.java)
                    emit(Resource.Error(errorResponseObj.message, statusCode, null))
                } else {
                    emit(Resource.Error(errorMessage ?: "Unknown error", statusCode, null))
                }
            } else {
                emit(Resource.Error(e.message ?: "Unknown error", 400, null))
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun fetchOrders(
        limit: Int? = null,
        me: Int? = null,
    ): Flow<Resource<out List<Order?>?>> = flow {
        try {
            val response = orderApiService.fetchOrders(limit = limit, me=me)
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
                        gson.fromJson(errorResponse, GetFetchOrdersResponse::class.java)
                    emit(Resource.Error(errorResponseObj.message, statusCode, null))
                } else {
                    emit(Resource.Error(errorMessage ?: "Unknown error", statusCode, null))
                }
            } else {
                emit(Resource.Error(e.message ?: "Unknown error", 400, null))
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun fetchOrders(
        minDate: String? = null,
        maxDate: String? = null,
        me: Int? = null,
    ): Flow<Resource<out Int?>> = flow {
        try {
            val response = orderApiService.fetchOrders(minDate = minDate, maxDate= maxDate, me=me)
            emit(Resource.Success(response.totalData, response.message, response.statusCode))
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
                        gson.fromJson(errorResponse, GetFetchOrdersResponse::class.java)
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