package com.dr.jjsembako.core.data.model

import com.google.gson.annotations.SerializedName

data class DataProductOrder(

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
    
    // For order purpose
    val orderQty: Int = 0,
    val orderPrice: Long = standardPrice,
    val orderTotalPrice: Long = orderPrice * orderQty,
    val isChosen: Boolean = false
)
