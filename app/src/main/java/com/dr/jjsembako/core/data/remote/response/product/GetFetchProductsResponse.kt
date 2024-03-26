package com.dr.jjsembako.core.data.remote.response.product

import com.google.gson.annotations.SerializedName

data class GetFetchProductsResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("data")
    val data: List<Product?>? = null,

    @field:SerializedName("totalData")
    val totalData: Int? = null,

    @field:SerializedName("count")
    val count: Int? = null
)

data class Product(

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
    val stockInPcsRemaining: Int
)
