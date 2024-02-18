package com.dr.jjsembako.feature_history.domain.repository

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.retur.DeleteHandleDeleteReturResponse
import com.dr.jjsembako.core.data.remote.response.retur.PostHandleCreateReturResponse
import kotlinx.coroutines.flow.Flow

interface IReturRepository {

    suspend fun handleCreateRetur(
        orderId: String,
        returedProductId: String,
        returnedProductId: String,
        amountInUnit: Int,
        selledPrice: Long
    ): Flow<Resource<out PostHandleCreateReturResponse>>

    suspend fun handleDeleteRetur(id: String): Flow<Resource<out DeleteHandleDeleteReturResponse>>

}