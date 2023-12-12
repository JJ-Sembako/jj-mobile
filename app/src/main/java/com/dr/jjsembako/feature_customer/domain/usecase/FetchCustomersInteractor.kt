package com.dr.jjsembako.feature_customer.domain.usecase

import androidx.paging.PagingData
import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.feature_customer.domain.repository.ICustomerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchCustomersInteractor @Inject constructor(private val customerRepository: ICustomerRepository) :
    FetchCustomersUseCase {
    override suspend fun fetchCustomers(
        search: String?,
        page: Int?,
        limit: Int?
    ): Flow<Resource<out List<DataCustomer?>>> =
        customerRepository.fetchCustomers(search, page, limit)

    override suspend fun getPager(searchQuery: String): Flow<PagingData<DataCustomer>> =
        customerRepository.getPager(searchQuery)
}