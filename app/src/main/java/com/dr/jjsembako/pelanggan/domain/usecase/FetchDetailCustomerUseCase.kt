package com.dr.jjsembako.pelanggan.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.pelanggan.domain.model.Customer
import kotlinx.coroutines.flow.Flow

interface FetchDetailCustomerUseCase {

    suspend fun fetchDetailCustomer(id: String): Flow<Resource<out Customer?>>

}