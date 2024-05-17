package com.dr.jjsembako.pelanggan.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.pelanggan.domain.model.Customer
import com.dr.jjsembako.pelanggan.domain.repository.ICustomerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HandleUpdateCustomerInteractor @Inject constructor(private val customerRepository: ICustomerRepository) :
    HandleUpdateCustomerUseCase {
    override suspend fun handleUpdateCustomer(
        id: String,
        name: String,
        shopName: String,
        address: String,
        gmapsLink: String,
        phoneNumber: String
    ): Flow<Resource<out Customer?>> = customerRepository.handleUpdateCustomer(
        id,
        name,
        shopName,
        address,
        gmapsLink,
        phoneNumber
    )
}