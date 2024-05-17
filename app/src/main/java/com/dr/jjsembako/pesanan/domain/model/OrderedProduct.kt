package com.dr.jjsembako.pesanan.domain.model

import com.dr.jjsembako.inventaris.domain.model.Product
import com.google.gson.annotations.SerializedName

data class OrderedProduct(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("amount")
    val amount: Int,

    @field:SerializedName("actual_amount")
    val actualAmount: Int,

    @field:SerializedName("selled_price")
    val selledPrice: Long,

    @field:SerializedName("status")
    val status: Int,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("product")
    val product: Product
)