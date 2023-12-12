package com.dr.jjsembako.core.data.remote.response.customer

import com.google.gson.annotations.SerializedName

data class PostHandleCreateCustomerResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("data")
	val data: DataCustomer? = null,
)
