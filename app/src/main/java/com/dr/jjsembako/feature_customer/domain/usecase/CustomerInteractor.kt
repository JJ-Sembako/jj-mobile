package com.dr.jjsembako.feature_customer.domain.usecase

import com.dr.jjsembako.feature_customer.domain.repository.ICustomerRepository
import javax.inject.Inject

class CustomerInteractor @Inject constructor(private val customerRepository: ICustomerRepository) :
    CustomerUseCase {
}