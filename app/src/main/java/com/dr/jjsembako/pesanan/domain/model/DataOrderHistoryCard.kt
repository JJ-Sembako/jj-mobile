package com.dr.jjsembako.pesanan.domain.model

import com.dr.jjsembako.pelanggan.domain.model.Customer
import com.dr.jjsembako.akun.domain.model.Account

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
