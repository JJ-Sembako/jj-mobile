package com.dr.jjsembako.pesanan.domain.usecase.create

import androidx.paging.PagingData
import com.dr.jjsembako.pelanggan.domain.model.Customer
import com.dr.jjsembako.pesanan.domain.repository.ISelectCustRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchSelectCustInteractor @Inject constructor(private val selectCustRepository: ISelectCustRepository) :
    FetchSelectCustUseCase {

    override suspend fun fetchCustomers(searchQuery: String): Flow<PagingData<Customer>> =
        selectCustRepository.fetchCustomers(searchQuery)
}