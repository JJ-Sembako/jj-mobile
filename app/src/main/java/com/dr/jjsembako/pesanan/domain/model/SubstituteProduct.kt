package com.dr.jjsembako.pesanan.domain.model

import com.google.gson.annotations.SerializedName

data class SubstituteProduct(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("image")
    val image: String = "",

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("unit")
    val unit: String,

    @field:SerializedName("standard_price")
    val standardPrice: Long,

    @field:SerializedName("amount_per_unit")
    val amountPerUnit: Int,

    @field:SerializedName("stock_in_pcs")
    val stockInPcs: Int,

    @field:SerializedName("stock_in_unit")
    val stockInUnit: Int,

    @field:SerializedName("stock_in_pcs_remaining")
    val stockInPcsRemaining: Int,

    // For retur substitute purpose
    val selledPrice: Long = 0,
    val isChosen: Boolean = false
)
