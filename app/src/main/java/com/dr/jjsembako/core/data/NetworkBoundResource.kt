package com.dr.jjsembako.core.data

import com.dr.jjsembako.core.common.ApiResponse
import com.dr.jjsembako.core.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

//abstract class NetworkBoundResource<ResultType, RequestType> {
//
//    private var result: Flow<Resource<ResultType>> = flow {
//        emit(Resource.Loading())
//        val dbSource = loadFromDB().first()
//        when (val apiResponse = createCall().first()) {
//            is ApiResponse.Success -> {
//                saveCallResult(apiResponse.data)
//                emitAll(loadFromDB().map { Resource.Success(it) })
//            }
//            is ApiResponse.Empty -> {
//                emitAll(loadFromDB().map { Resource.Success(it) })
//            }
//            is ApiResponse.Error -> {
//                onFetchFailed()
//                emit(Resource.Error<ResultType>(apiResponse.errorMessage))
//            }
//        }
//    }
//
//    protected open fun onFetchFailed() {}
//
//    protected abstract fun loadFromDB(): Flow<ResultType>
//
//    protected abstract fun shouldFetch(data: ResultType?): Boolean
//
//    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>
//
//    protected abstract suspend fun saveCallResult(data: RequestType)
//
//    fun asFlow(): Flow<Resource<ResultType>> = result
//}