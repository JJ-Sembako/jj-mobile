package com.dr.jjsembako.feature_history.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.order.DetailOrderData
import com.dr.jjsembako.core.data.remote.response.order.GetFetchOrderResponse
import com.dr.jjsembako.core.data.remote.response.order.OrderDataItem
import com.dr.jjsembako.feature_history.domain.repository.ICanceledRepository
import com.dr.jjsembako.feature_history.domain.repository.IHistoryRepository
import com.dr.jjsembako.feature_history.domain.repository.IReturRepository
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
class HistoryRepository @Inject constructor(
    private val historyDataSource: HistoryDataSource,
    private val historyPagingSource: HistoryPagingSource,
    private val gson: Gson
) : IHistoryRepository, ICanceledRepository, IReturRepository {

    override suspend fun fetchOrders(
        search: String?,
        minDate: String?,
        maxDate: String?,
        me: Int?
    ): Flow<PagingData<OrderDataItem>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                historyPagingSource.apply {
                    setParams(search = search, minDate = minDate, maxDate = maxDate, me = me)
                }
            }
        ).flow
    }

    override suspend fun fetchOrder(id: String): Flow<Resource<out DetailOrderData?>> = flow {
        emit(Resource.Loading())
        try {
            val response = historyDataSource.fetchOrder(id).first()

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

}