package com.dr.jjsembako.feature_customer.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.customer.Customer
import com.dr.jjsembako.feature_customer.domain.repository.ICustomerRepository
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