package com.dr.jjsembako.pesanan.domain.usecase.create

import androidx.paging.PagingData
import com.dr.jjsembako.pelanggan.domain.model.Customer
import kotlinx.coroutines.flow.Flow

interface FetchSelectCustUseCase {

    suspend fun fetchCustomers(searchQuery: String = ""): Flow<PagingData<Customer>>

}