package com.dr.jjsembako.feature_order.data

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.network.CustomerApiService
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.core.data.remote.response.customer.GetFetchDetailCustomerResponse
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
    private val gson: Gson
) {

    suspend fun fetchDetailCustomer(id: String): Flow<Resource<out DataCustomer?>> = flow {
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