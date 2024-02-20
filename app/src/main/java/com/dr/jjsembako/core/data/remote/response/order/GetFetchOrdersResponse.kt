package com.dr.jjsembako.core.data.remote.response.order

import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.core.data.remote.response.product.DataProduct
import com.google.gson.annotations.SerializedName

data class GetFetchOrdersResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("data")
    val data: List<OrderDataItem>? = null,

    @field:SerializedName("count")
    val count: Int? = null,

    @field:SerializedName("totalData")
    val totalData: Int? = null,
)

data class OrderDataItem(

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
    val customer: DataCustomer,

    @field:SerializedName("orderToProducts")
    val orderToProducts: List<OrderToProductsItem>,

    @field:SerializedName("retur")
    val retur: Int,

    @field:SerializedName("canceled")
    val canceled: Int
)

data class Account(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("role")
    val role: String,

    @field:SerializedName("username")
    val username: String
)

data class OrderToProductsItem(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("amount")
    val amount: Int,

    @field:SerializedName("actual_amount")
    val actualAmount: Int,

//    @field:SerializedName("is_confirmed")
//    val isConfirmed: Boolean,

    @field:SerializedName("selled_price")
    val selledPrice: Long,

    @field:SerializedName("status")
    val status: Int,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("product")
    val product: DataProduct
)
