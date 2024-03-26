package com.dr.jjsembako.feature_history.domain.model

import com.dr.jjsembako.core.data.remote.response.customer.Customer
import com.dr.jjsembako.core.data.remote.response.order.Account

data class DataOrderHistoryCard(
    val id: String,
    val invoice: String,
    val orderStatus: Int,
    val paymentStatus: Int,
    val totalPrice: Long,
    val createdAt: String,
    val account: Account,
    val customer: Customer
)
