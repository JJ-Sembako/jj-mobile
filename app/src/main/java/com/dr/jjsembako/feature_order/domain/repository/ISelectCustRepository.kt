package com.dr.jjsembako.feature_order.domain.repository

import androidx.paging.PagingData
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.customer.Customer
import kotlinx.coroutines.flow.Flow

interface ISelectCustRepository {

    suspend fun fetchCustomers(searchQuery: String = ""): Flow<PagingData<Customer>>

    suspend fun fetchDetailCustomer(id: String): Flow<Resource<out Customer?>>

}