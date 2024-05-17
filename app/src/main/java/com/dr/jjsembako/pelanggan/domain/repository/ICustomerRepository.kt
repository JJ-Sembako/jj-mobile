package com.dr.jjsembako.pelanggan.domain.repository

import androidx.paging.PagingData
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.pelanggan.domain.model.Customer
import com.dr.jjsembako.core.data.remote.response.customer.DeleteHandleDeleteCustomerResponse
import com.dr.jjsembako.pesanan.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface ICustomerRepository {

    suspend fun fetchCustomers(searchQuery: String = ""): Flow<PagingData<Customer>>

    suspend fun handleCreateCustomer(
        name: String,
        shopName: String,
        address: String,
        gmapsLink: String,
        phoneNumber: String
    ): Flow<Resource<out Customer?>>

    suspend fun fetchDetailCustomer(id: String): Flow<Resource<out Customer?>>

    suspend fun handleUpdateCustomer(
        id: String,
        name: String,
        shopName: String,
        address: String,
        gmapsLink: String,
        phoneNumber: String
    ): Flow<Resource<out Customer?>>

    suspend fun handleDeleteCustomer(id: String): Flow<Resource<out DeleteHandleDeleteCustomerResponse>>

    suspend fun fetchOrders(
        search: String? = null,
        minDate: String? = null,
        maxDate: String? = null,
        me: Int? = null,
        customerId: String
    ): Flow<PagingData<Order>>
}