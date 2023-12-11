package com.dr.jjsembako.feature_customer.domain.usecase

import com.dr.jjsembako.feature_customer.domain.repository.ICustomerRepository
import javax.inject.Inject

class HandleCreateCustomerInteractor @Inject constructor(private val customerRepository: ICustomerRepository) :
    HandleCreateCustomerUseCase {
    override suspend fun handleCreateCustomer(
        name: String,
        shopName: String,
        address: String,
        gmapsLink: String,
        phoneNumber: String
    ) = customerRepository.handleCreateCustomer(name, shopName, address, gmapsLink, phoneNumber)
}