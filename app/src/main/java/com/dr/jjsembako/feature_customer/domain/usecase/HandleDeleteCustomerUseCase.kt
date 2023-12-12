package com.dr.jjsembako.feature_customer.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.customer.DeleteHandleDeleteCustomerResponse
import kotlinx.coroutines.flow.Flow

interface HandleDeleteCustomerUseCase {

    suspend fun handleDeleteCustomer(id: String): Flow<Resource<out DeleteHandleDeleteCustomerResponse>>

}