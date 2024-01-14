package com.dr.jjsembako.feature_order.domain.usecase

import androidx.paging.PagingData
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.feature_order.domain.repository.ISelectCustRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchSelectCustInteractor @Inject constructor(private val selectCustRepository: ISelectCustRepository) :
    FetchSelectCustUseCase {

    override suspend fun fetchCustomers(searchQuery: String): Flow<PagingData<DataCustomer>> =
        selectCustRepository.fetchCustomers(searchQuery)
}