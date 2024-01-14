package com.dr.jjsembako.feature_order.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.feature_order.domain.repository.ISelectCustRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val selectCustPagingSource: SelectCustPagingSource,
    private val gson: Gson
) : ISelectCustRepository {

    override suspend fun fetchCustomers(searchQuery: String): Flow<PagingData<DataCustomer>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = { selectCustPagingSource.apply { setSearchQuery(searchQuery) } }
        ).flow
    }

}