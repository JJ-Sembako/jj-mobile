package com.dr.jjsembako.core.data.remote.response.customer

import com.dr.jjsembako.pelanggan.domain.model.Customer
import com.google.gson.annotations.SerializedName

data class GetFetchCustomersResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("data")
    val data: List<Customer>? = null,

    @field:SerializedName("count")
    val count: Int? = null,
)