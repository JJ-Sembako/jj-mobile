package com.dr.jjsembako.feature_order.domain.repository

import androidx.paging.PagingData
import com.dr.jjsembako.core.data.model.DataCustomerOrder
import kotlinx.coroutines.flow.Flow

interface ISelectCustRepository {

    suspend fun fetchCustomers(searchQuery: String = ""): Flow<PagingData<DataCustomerOrder>>

}