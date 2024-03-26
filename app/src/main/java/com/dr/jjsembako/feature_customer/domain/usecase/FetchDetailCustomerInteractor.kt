package com.dr.jjsembako.feature_customer.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.customer.Customer
import com.dr.jjsembako.feature_customer.domain.repository.ICustomerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchDetailCustomerInteractor @Inject constructor(private val customerRepository: ICustomerRepository) :
    FetchDetailCustomerUseCase {
    override suspend fun fetchDetailCustomer(id: String): Flow<Resource<out Customer?>> =
        customerRepository.fetchDetailCustomer(id)
}