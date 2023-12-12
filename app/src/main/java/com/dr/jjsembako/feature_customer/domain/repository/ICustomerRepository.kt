package com.dr.jjsembako.feature_customer.domain.repository

import androidx.paging.PagingData
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.core.data.remote.response.customer.DeleteHandleDeleteCustomerResponse
import kotlinx.coroutines.flow.Flow

interface ICustomerRepository {

    suspend fun fetchCustomers(searchQuery: String = ""): Flow<PagingData<DataCustomer>>

    suspend fun handleCreateCustomer(
        name: String,
        shopName: String,
        address: String,
        gmapsLink: String,
        phoneNumber: String
    ): Flow<Resource<out DataCustomer?>>

    suspend fun fetchDetailCustomer(id: String): Flow<Resource<out DataCustomer?>>

    suspend fun handleUpdateCustomer(
        id: String,
        name: String,
        shopName: String,
        address: String,
        gmapsLink: String,
        phoneNumber: String
    ): Flow<Resource<out DataCustomer?>>

    suspend fun handleDeleteCustomer(id: String): Flow<Resource<out DeleteHandleDeleteCustomerResponse>>
}