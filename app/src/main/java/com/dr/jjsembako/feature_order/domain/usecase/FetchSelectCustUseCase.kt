package com.dr.jjsembako.feature_order.domain.usecase

import androidx.paging.PagingData
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import kotlinx.coroutines.flow.Flow

interface FetchSelectCustUseCase {

    suspend fun fetchCustomers(searchQuery: String = ""): Flow<PagingData<DataCustomer>>

}