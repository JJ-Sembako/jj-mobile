package com.dr.jjsembako.core.data.model

import com.google.gson.annotations.SerializedName

data class OrderProduct(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("amountInUnit")
    val amountInUnit: Int,

    @field:SerializedName("pricePerUnit")
    val pricePerUnitL: Long
)

data class OrderRequest(

    @field:SerializedName("customerId")
    val customerId: String,

    @field:SerializedName("products")
    val products: List<OrderProduct>,

    @field:SerializedName("paymentStatus")
    val paymentStatus: String
)
