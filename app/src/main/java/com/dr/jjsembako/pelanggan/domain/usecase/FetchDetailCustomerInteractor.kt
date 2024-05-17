package com.dr.jjsembako.pelanggan.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.pelanggan.domain.model.Customer
import com.dr.jjsembako.pelanggan.domain.repository.ICustomerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchDetailCustomerInteractor @Inject constructor(private val customerRepository: ICustomerRepository) :
    FetchDetailCustomerUseCase {
    override suspend fun fetchDetailCustomer(id: String): Flow<Resource<out Customer?>> =
        customerRepository.fetchDetailCustomer(id)
}