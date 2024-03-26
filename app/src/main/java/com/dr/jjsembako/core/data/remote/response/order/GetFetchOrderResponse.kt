package com.dr.jjsembako.core.data.remote.response.order

import com.dr.jjsembako.core.data.remote.response.customer.Customer
import com.dr.jjsembako.core.data.remote.response.product.DataProduct
import com.google.gson.annotations.SerializedName

data class GetFetchOrderResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("data")
    val data: DataDetailOrder? = null
)

data class DataDetailOrder(

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
    val orderToProducts: List<OrderToProductsItem>,

    @field:SerializedName("canceled")
    val canceled: List<CanceledItem?>? = null,

    @field:SerializedName("retur")
    val retur: List<ReturItem?>? = null
)

data class CanceledItem(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("amount")
    val amount: Int,

    @field:SerializedName("status")
    val status: Int,

    @field:SerializedName("selled_price")
    val selledPrice: Long,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("product")
    val product: DataProduct
)

data class ReturItem(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("amount")
    val amount: Int,

    @field:SerializedName("status")
    val status: Int,

    @field:SerializedName("old_selled_price")
    val oldSelledPrice: Long,

    @field:SerializedName("selled_price")
    val selledPrice: Long,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("retured_product")
    val returedProduct: DataProduct,

    @field:SerializedName("returned_product")
    val returnedProduct: DataProduct
)