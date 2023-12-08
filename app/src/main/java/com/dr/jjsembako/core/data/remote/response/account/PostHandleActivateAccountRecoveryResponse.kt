package com.dr.jjsembako.core.data.remote.response.account

import com.google.gson.annotations.SerializedName

data class PostHandleActivateAccountRecoveryResponse(

	@field:SerializedName("data")
	val data: DataActivateAccountRecovery? = null,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int
)

data class DataActivateAccountRecovery(

	@field:SerializedName("is_active")
	val isActive: Boolean,

	@field:SerializedName("answer")
	val answer: String,

	@field:SerializedName("question")
	val question: String,

	@field:SerializedName("id")
	val id: String
)
