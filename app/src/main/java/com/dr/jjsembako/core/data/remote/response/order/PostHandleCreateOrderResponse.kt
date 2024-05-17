package com.dr.jjsembako.core.data.remote.response.order

import com.dr.jjsembako.akun.domain.model.Account
import com.dr.jjsembako.pelanggan.domain.model.Customer
import com.dr.jjsembako.pesanan.domain.model.OrderedProduct
import com.google.gson.annotations.SerializedName

data class PostHandleCreateOrderResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("data")
    val data: DataAfterCreateOrder? = null,
)
data class DataAfterCreateOrder(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("invoice")
    val invoice: String,

    @field:SerializedName("account")
    val account: Account,

    @field:SerializedName("customer")
    val customer: Customer,

    @field:SerializedName("order_status")
    val orderStatus: Int,

    @field:SerializedName("payment_status")
    val paymentStatus: Int,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("total_price")
    val totalPrice: Long,

    @field:SerializedName("actual_total_price")
    val actualTotalPrice: Long,

    @field:SerializedName("orderToProducts")
    val orderToProducts: List<OrderedProduct>
)