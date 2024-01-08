package com.dr.jjsembako.feature_warehouse.data

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.category.DataCategory
import com.dr.jjsembako.core.data.remote.response.category.GetFetchCategoriesResponse
import com.dr.jjsembako.feature_warehouse.domain.repository.IWarehouseRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

class WarehouseRepository @Inject constructor(private val warehouseDataSource: WarehouseDataSource) :
    IWarehouseRepository {
    override suspend fun fetchCategories(): Flow<Resource<out List<DataCategory?>>> = flow {
        emit(Resource.Loading())
        try {
            val response = warehouseDataSource.fetchCategories().first()

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
                        Gson().fromJson(errorResponse, GetFetchCategoriesResponse::class.java)
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