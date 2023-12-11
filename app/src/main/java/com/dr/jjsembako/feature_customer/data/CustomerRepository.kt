package com.dr.jjsembako.feature_customer.data

import javax.inject.Inject

class CustomerRepository @Inject constructor(private val customerDataSource: CustomerDataSource) {
}