package com.dr.jjsembako.feature_home.data

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.GetFetchOrdersResponse
import com.dr.jjsembako.core.data.remote.response.order.OrderDataItem
import com.dr.jjsembako.core.data.remote.response.performance.GetFetchOmzetMonthlyResponse
import com.dr.jjsembako.core.data.remote.response.performance.GetFetchSelledProductResponse
import com.dr.jjsembako.core.data.remote.response.performance.OmzetData
import com.dr.jjsembako.core.data.remote.response.performance.SelledData
import com.dr.jjsembako.feature_home.domain.repository.IHomeRepository
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
class HomeRepository @Inject constructor(
    private val homeDataSource: HomeDataSource,
    private val gson: Gson
) : IHomeRepository {
    override suspend fun fetchOmzetMonthly(month: Int, year: Int): Flow<Resource<out OmzetData?>> =
        flow {
            emit(Resource.Loading())
            try {
                val response = homeDataSource.fetchOmzetMonthly(month, year).first()

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

    override suspend fun fetchSelledProductMonthly(
        month: Int,
        year: Int
    ): Flow<Resource<out List<SelledData?>?>> = flow {
        emit(Resource.Loading())
        try {
            val response = homeDataSource.fetchSelledProductMonthly(month, year).first()

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

    override suspend fun fetchOrders(
        limit: Int?,
        me: Int?
    ): Flow<Resource<out List<OrderDataItem?>?>> = flow {
        emit(Resource.Loading())
        try {
            val response = homeDataSource.fetchOrders(limit, me).first()

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

    override suspend fun fetchOrders(
        minDate: String?,
        maxDate: String?,
        me: Int?
    ): Flow<Resource<out Int?>> = flow {
        emit(Resource.Loading())
        try {
            val response = homeDataSource.fetchOrders(minDate, maxDate, me).first()

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