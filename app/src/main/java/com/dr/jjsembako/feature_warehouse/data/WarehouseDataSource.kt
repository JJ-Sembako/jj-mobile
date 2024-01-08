package com.dr.jjsembako.feature_warehouse.data

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.network.CategoryApiService
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleUpdateSelfPasswordResponse
import com.dr.jjsembako.core.data.remote.response.category.DataCategory
import com.dr.jjsembako.core.data.remote.response.category.GetFetchCategoriesResponse
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class WarehouseDataSource @Inject constructor(private val categoryApiService: CategoryApiService) {
    suspend fun fetchCategories(): Flow<Resource<out List<DataCategory?>>> =
        flow {
            try {
                val response = categoryApiService.fetchCategories()
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
                            Gson().fromJson(
                                errorResponse,
                                GetFetchCategoriesResponse::class.java
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