package com.dr.jjsembako.feature_order.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.customer.Customer
import com.dr.jjsembako.feature_order.domain.repository.ISelectCustRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchDetailSelectedCustInteractor @Inject constructor(private val selectCustRepository: ISelectCustRepository) :
    FetchDetailSelectedCustUseCase {
    override suspend fun fetchDetailCustomer(id: String): Flow<Resource<out Customer?>> =
        selectCustRepository.fetchDetailCustomer(id)
}