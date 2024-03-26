package com.dr.jjsembako.feature_customer.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.customer.Customer
import kotlinx.coroutines.flow.Flow

interface FetchDetailCustomerUseCase {

    suspend fun fetchDetailCustomer(id: String): Flow<Resource<out Customer?>>

}