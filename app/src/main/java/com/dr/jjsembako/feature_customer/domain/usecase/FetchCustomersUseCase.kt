package com.dr.jjsembako.feature_customer.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import kotlinx.coroutines.flow.Flow

interface FetchCustomersUseCase {

    suspend fun fetchCustomers(
        search: String? = null,
        page: Int? = null,
        limit: Int? = null
    ): Flow<Resource<out List<DataCustomer?>>>

}