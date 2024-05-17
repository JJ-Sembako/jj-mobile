package com.dr.jjsembako.pesanan.domain.usecase.create

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.pelanggan.domain.model.Customer
import com.dr.jjsembako.pesanan.domain.repository.ISelectCustRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchDetailSelectedCustInteractor @Inject constructor(private val selectCustRepository: ISelectCustRepository) :
    FetchDetailSelectedCustUseCase {
    override suspend fun fetchDetailCustomer(id: String): Flow<Resource<out Customer?>> =
        selectCustRepository.fetchDetailCustomer(id)
}