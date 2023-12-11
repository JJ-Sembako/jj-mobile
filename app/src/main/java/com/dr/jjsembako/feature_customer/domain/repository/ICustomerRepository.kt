package com.dr.jjsembako.feature_customer.domain.repository

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.core.data.remote.response.customer.DeleteHandleDeleteCustomerResponse
import com.dr.jjsembako.core.data.remote.response.customer.PostHandleCreateCustomerResponse
import com.dr.jjsembako.core.data.remote.response.customer.PutHandleUpdateCustomerResponse
import kotlinx.coroutines.flow.Flow

interface ICustomerRepository {

    suspend fun fetchCustomers(
        search: String? = null,
        page: Int? = null,
        limit: Int? = null
    ): Flow<Resource<out List<DataCustomer?>>>

    suspend fun handleCreateCustomer(
        name: String,
        shopName: String,
        address: String,
        gmapsLink: String,
        phoneNumber: String
    ): Flow<Resource<PostHandleCreateCustomerResponse>>

    suspend fun fetchDetailCustomer(id: String): Flow<Resource<out DataCustomer?>>

    suspend fun handleUpdateCustomer(
        id: String,
        name: String,
        shopName: String,
        address: String,
        gmapsLink: String,
        phoneNumber: String
    ): Flow<Resource<PutHandleUpdateCustomerResponse>>

    suspend fun handleDeleteCustomer(id: String): Flow<Resource<DeleteHandleDeleteCustomerResponse>>
}