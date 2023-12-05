package com.dr.jjsembako.core.domain.usecase

import com.dr.jjsembako.core.domain.repository.ICustomerRepository
import javax.inject.Inject

class CustomerInteractor @Inject constructor(private val customerRepository: ICustomerRepository) :
    CustomerUseCase {
}