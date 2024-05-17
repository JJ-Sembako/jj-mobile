package com.dr.jjsembako.core.data.remote.response.product

import com.dr.jjsembako.inventaris.domain.model.Product
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