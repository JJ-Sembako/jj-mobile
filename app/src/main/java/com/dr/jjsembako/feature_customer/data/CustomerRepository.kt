package com.dr.jjsembako.feature_customer.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.core.data.remote.response.customer.DeleteHandleDeleteCustomerResponse
import com.dr.jjsembako.core.data.remote.response.customer.GetFetchDetailCustomerResponse
import com.dr.jjsembako.core.data.remote.response.customer.PostHandleCreateCustomerResponse
import com.dr.jjsembako.core.data.remote.response.customer.PutHandleUpdateCustomerResponse
import com.dr.jjsembako.core.data.remote.response.order.OrderDataItem
import com.dr.jjsembako.feature_customer.domain.repository.ICustomerRepository
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
class CustomerRepository @Inject constructor(
    private val customerDataSource: CustomerDataSource,
    private val customerPagingSource: CustomerPagingSource,
    private val custOrdersPagingSource: CustOrdersPagingSource,
    private val gson: Gson
) : ICustomerRepository {

    override suspend fun fetchCustomers(searchQuery: String): Flow<PagingData<DataCustomer>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = { customerPagingSource.apply { setSearchQuery(searchQuery) } }
        ).flow
    }

    override suspend fun handleCreateCustomer(
        name: String,
        shopName: String,
        address: String,
        gmapsLink: String,
        phoneNumber: String
    ): Flow<Resource<out DataCustomer?>> = flow {
        emit(Resource.Loading())
        try {
            val response = customerDataSource.handleCreateCustomer(
                name,
                shopName,
                address,
                gmapsLink,
                phoneNumber
            ).first()

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
                        gson.fromJson(errorResponse, PostHandleCreateCustomerResponse::class.java)
                    emit(Resource.Error(errorResponseObj.message, statusCode, null))
                } else {
                    emit(Resource.Error(errorMessage ?: "Unknown error", statusCode, null))
                }
            } else {
                emit(Resource.Error(e.message ?: "Unknown error", 400, null))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun fetchDetailCustomer(id: String): Flow<Resource<out DataCustomer?>> = flow {
        emit(Resource.Loading())
        try {
            val response = customerDataSource.fetchDetailCustomer(id).first()

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

    override suspend fun handleUpdateCustomer(
        id: String,
        name: String,
        shopName: String,
        address: String,
        gmapsLink: String,
        phoneNumber: String
    ): Flow<Resource<out DataCustomer?>> = flow {
        emit(Resource.Loading())
        try {
            val response = customerDataSource.handleUpdateCustomer(
                id,
                name,
                shopName,
                address,
                gmapsLink,
                phoneNumber
            ).first()

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
                        gson.fromJson(errorResponse, PutHandleUpdateCustomerResponse::class.java)
                    emit(Resource.Error(errorResponseObj.message, statusCode, null))
                } else {
                    emit(Resource.Error(errorMessage ?: "Unknown error", statusCode, null))
                }
            } else {
                emit(Resource.Error(e.message ?: "Unknown error", 400, null))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun handleDeleteCustomer(id: String): Flow<Resource<out DeleteHandleDeleteCustomerResponse>> =
        flow {
            emit(Resource.Loading())
            try {
                val response = customerDataSource.handleDeleteCustomer(id).first()

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
                            gson.fromJson(
                                errorResponse,
                                DeleteHandleDeleteCustomerResponse::class.java
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

    override suspend fun fetchOrders(
        search: String?,
        minDate: String?,
        maxDate: String?,
        me: Int?,
        customerId: String
    ): Flow<PagingData<OrderDataItem>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                custOrdersPagingSource.apply {
                    setParams(
                        search = search, minDate = minDate, maxDate = maxDate,
                        me = me, customerId = customerId
                    )
                }
            }
        ).flow
    }

}