package com.dr.jjsembako.pesanan.domain.usecase.create

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.pelanggan.domain.model.Customer
import kotlinx.coroutines.flow.Flow

interface FetchDetailSelectedCustUseCase {

    suspend fun fetchDetailCustomer(id: String): Flow<Resource<out Customer?>>

}