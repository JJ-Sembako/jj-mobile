package com.dr.jjsembako.pesanan.domain.model

import com.dr.jjsembako.inventaris.domain.model.Product
import com.google.gson.annotations.SerializedName

data class Canceled(

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
    val product: Product
)