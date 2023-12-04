package com.dr.jjsembako.core.data.remote.response.account

import com.google.gson.annotations.SerializedName

data class PostHandleDeactivateAccountRecoveryResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int
)
