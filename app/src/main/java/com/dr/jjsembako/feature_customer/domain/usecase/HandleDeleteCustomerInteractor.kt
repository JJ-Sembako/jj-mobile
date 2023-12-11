package com.dr.jjsembako.feature_customer.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.customer.DeleteHandleDeleteCustomerResponse
import com.dr.jjsembako.feature_customer.domain.repository.ICustomerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HandleDeleteCustomerInteractor @Inject constructor(private val customerRepository: ICustomerRepository) :
    HandleDeleteCustomerUseCase {
    override suspend fun handleDeleteCustomer(id: String): Flow<Resource<out DeleteHandleDeleteCustomerResponse>> =
        customerRepository.handleDeleteCustomer(id)
}