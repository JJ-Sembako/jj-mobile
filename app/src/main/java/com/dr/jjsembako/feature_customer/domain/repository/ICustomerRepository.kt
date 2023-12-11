package com.dr.jjsembako.feature_customer.domain.repository

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import kotlinx.coroutines.flow.Flow

interface ICustomerRepository {

    suspend fun fetchCustomers(
        search: String? = null,
        page: Int? = null,
        limit: Int? = null
    ): Flow<Resource<out DataCustomer?>>

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

    suspend fun handleDeleteCustomer(id: String): Flow<Resource<out DataCustomer?>>
}