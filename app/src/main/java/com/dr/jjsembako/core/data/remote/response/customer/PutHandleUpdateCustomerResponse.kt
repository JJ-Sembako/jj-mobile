package com.dr.jjsembako.core.data.remote.response.customer

import com.google.gson.annotations.SerializedName

data class PutHandleUpdateCustomerResponse(

    @field:SerializedName("message")
	val message: String,

    @field:SerializedName("statusCode")
	val statusCode: Int,

    @field:SerializedName("data")
	val data: Customer? = null,
)