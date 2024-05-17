package com.dr.jjsembako.pesanan.domain.model

import com.dr.jjsembako.akun.domain.model.Account
import com.dr.jjsembako.pelanggan.domain.model.Customer
import com.google.gson.annotations.SerializedName

data class DetailOrder(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("invoice")
    val invoice: String,

    @field:SerializedName("order_status")
    val orderStatus: Int,

    @field:SerializedName("payment_status")
    val paymentStatus: Int,

    @field:SerializedName("total_price")
    val totalPrice: Long,

    @field:SerializedName("actual_total_price")
    val actualTotalPrice: Long,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("deliver_at")
    val deliverAt: String? = null,

    @field:SerializedName("finished_at")
    val finishedAt: String? = null,

    @field:SerializedName("account")
    val account: Account,

    @field:SerializedName("customer")
    val customer: Customer,

    @field:SerializedName("orderToProducts")
    val orderToProducts: List<OrderedProduct>,

    @field:SerializedName("canceled")
    val canceled: List<Canceled?>? = null,

    @field:SerializedName("retur")
    val retur: List<Retur?>? = null
)