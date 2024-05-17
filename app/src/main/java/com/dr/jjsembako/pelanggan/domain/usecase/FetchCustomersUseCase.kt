package com.dr.jjsembako.pelanggan.domain.usecase

import androidx.paging.PagingData
import com.dr.jjsembako.pelanggan.domain.model.Customer
import kotlinx.coroutines.flow.Flow

interface FetchCustomersUseCase {

    suspend fun fetchCustomers(searchQuery: String = ""): Flow<PagingData<Customer>>

}