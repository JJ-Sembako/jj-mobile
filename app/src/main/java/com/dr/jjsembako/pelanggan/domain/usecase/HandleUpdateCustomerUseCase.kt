package com.dr.jjsembako.pelanggan.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.pelanggan.domain.model.Customer
import kotlinx.coroutines.flow.Flow

interface HandleUpdateCustomerUseCase {

    suspend fun handleUpdateCustomer(
        id: String,
        name: String,
        shopName: String,
        address: String,
        gmapsLink: String,
        phoneNumber: String
    ): Flow<Resource<out Customer?>>

}