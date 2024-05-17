package com.dr.jjsembako.pelanggan.domain.usecase

import androidx.paging.PagingData
import com.dr.jjsembako.pelanggan.domain.model.Customer
import com.dr.jjsembako.pelanggan.domain.repository.ICustomerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchCustomersInteractor @Inject constructor(private val customerRepository: ICustomerRepository) :
    FetchCustomersUseCase {

    override suspend fun fetchCustomers(searchQuery: String): Flow<PagingData<Customer>> =
        customerRepository.fetchCustomers(searchQuery)
}