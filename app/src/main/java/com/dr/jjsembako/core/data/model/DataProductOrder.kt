package com.dr.jjsembako.core.data.model

import com.google.gson.annotations.SerializedName

data class DataProductOrder(

    @field:SerializedName("id")
    var id: String,

    @field:SerializedName("name")
    var name: String,

    @field:SerializedName("image")
    var image: String = "",

    @field:SerializedName("category")
    var category: String,

    @field:SerializedName("unit")
    var unit: String,

    @field:SerializedName("standard_price")
    var standardPrice: Long,

    @field:SerializedName("amount_per_unit")
    var amountPerUnit: Int,

    @field:SerializedName("stock_in_pcs")
    var stockInPcs: Int,

    @field:SerializedName("stock_in_unit")
    var stockInUnit: Int,

    @field:SerializedName("stock_in_pcs_remaining")
    var stockInPcsRemaining: Int,
    
    // For order purpose
    var orderQty: Int = 0,
    var orderPrice: Long = standardPrice,
    var orderTotalPrice: Long = orderPrice * orderQty,
    var isChosen: Boolean = false
)
