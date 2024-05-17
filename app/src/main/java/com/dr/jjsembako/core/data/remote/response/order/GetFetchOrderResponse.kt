package com.dr.jjsembako.core.data.remote.response.order

import com.dr.jjsembako.pesanan.domain.model.DetailOrder
import com.google.gson.annotations.SerializedName

data class GetFetchOrderResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("data")
    val data: DetailOrder? = null
)