package com.dr.jjsembako.feature_order.data

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.model.OrderRequest
import com.dr.jjsembako.core.data.remote.network.CustomerApiService
import com.dr.jjsembako.core.data.remote.network.OrderApiService
import com.dr.jjsembako.core.data.remote.response.customer.Customer
import com.dr.jjsembako.core.data.remote.response.customer.GetFetchDetailCustomerResponse
import com.dr.jjsembako.core.data.remote.response.order.DataAfterCreateOrder
import com.dr.jjsembako.core.data.remote.response.order.PostHandleCreateOrderResponse
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class OrderDataSource @Inject constructor(
    private val customerApiService: CustomerApiService,
    private val orderApiService: OrderApiService,
    private val gson: Gson
) {

    suspend fun handleCreateOrder(orderRequest: OrderRequest): Flow<Resource<out DataAfterCreateOrder?>> =
        flow {
            try {
                val response = orderApiService.handleCreateOrder(orderRequest)
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
                            gson.fromJson(errorResponse, PostHandleCreateOrderResponse::class.java)
                        emit(Resource.Error(errorResponseObj.message, statusCode, null))
                    } else {
                        emit(Resource.Error(errorMessage ?: "Unknown error", statusCode, null))
                    }
                } else {
                    emit(Resource.Error(e.message ?: "Unknown error", 400, null))
                }
            }
        }.flowOn(Dispatchers.IO)

    suspend fun fetchDetailCustomer(id: String): Flow<Resource<out Customer?>> = flow {
        try {
            val response = customerApiService.fetchDetailCustomer(id)
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
                        gson.fromJson(errorResponse, GetFetchDetailCustomerResponse::class.java)
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