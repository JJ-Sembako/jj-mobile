package com.dr.jjsembako.feature_customer.data

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.network.CustomerApiService
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CustomerDataSource @Inject constructor(private val customerApiService: CustomerApiService) {

}