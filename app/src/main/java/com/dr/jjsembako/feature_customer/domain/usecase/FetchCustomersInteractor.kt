package com.dr.jjsembako.feature_customer.domain.usecase

import androidx.paging.PagingData
import com.dr.jjsembako.core.data.remote.response.customer.Customer
import com.dr.jjsembako.feature_customer.domain.repository.ICustomerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchCustomersInteractor @Inject constructor(private val customerRepository: ICustomerRepository) :
    FetchCustomersUseCase {

    override suspend fun fetchCustomers(searchQuery: String): Flow<PagingData<Customer>> =
        customerRepository.fetchCustomers(searchQuery)
}