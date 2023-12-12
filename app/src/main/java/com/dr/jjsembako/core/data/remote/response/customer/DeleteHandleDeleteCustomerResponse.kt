package com.dr.jjsembako.core.data.remote.response.customer

import com.google.gson.annotations.SerializedName

data class DeleteHandleDeleteCustomerResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int
)
