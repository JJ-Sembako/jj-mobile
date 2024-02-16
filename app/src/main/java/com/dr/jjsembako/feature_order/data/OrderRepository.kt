package com.dr.jjsembako.feature_order.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.model.OrderRequest
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.core.data.remote.response.customer.GetFetchDetailCustomerResponse
import com.dr.jjsembako.core.data.remote.response.order.DataAfterCreateOrder
import com.dr.jjsembako.core.data.remote.response.order.PostHandleCreateOrderResponse
import com.dr.jjsembako.feature_order.domain.repository.IOrderRepository
import com.dr.jjsembako.feature_order.domain.repository.ISelectCustRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderDataSource: OrderDataSource,
    private val selectCustPagingSource: SelectCustPagingSource,
    private val gson: Gson
) : ISelectCustRepository, IOrderRepository {

    override suspend fun fetchCustomers(searchQuery: String): Flow<PagingData<DataCustomer>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = { selectCustPagingSource.apply { setSearchQuery(searchQuery) } }
        ).flow
    }

    override suspend fun fetchDetailCustomer(id: String): Flow<Resource<out DataCustomer?>> = flow {
        emit(Resource.Loading())
        try {
            val response = orderDataSource.fetchDetailCustomer(id).first()

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

    override suspend fun handleCreateOrder(orderRequest: OrderRequest): Flow<Resource<out DataAfterCreateOrder?>> =
        flow {
            emit(Resource.Loading())
            try {
                val response =
                    orderDataSource.handleCreateOrder(orderRequest).first()

                when (response.status) {
                    201 -> {
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

}