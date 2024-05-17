package com.dr.jjsembako.core.data.remote.response.order

import com.dr.jjsembako.pesanan.domain.model.Order
import com.google.gson.annotations.SerializedName

data class GetFetchOrdersResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("data")
    val data: List<Order>? = null,

    @field:SerializedName("count")
    val count: Int? = null,

    @field:SerializedName("totalData")
    val totalData: Int? = null,
)