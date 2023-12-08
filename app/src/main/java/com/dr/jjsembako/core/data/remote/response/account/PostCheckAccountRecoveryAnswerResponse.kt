package com.dr.jjsembako.core.data.remote.response.account

import com.google.gson.annotations.SerializedName

data class PostCheckAccountRecoveryAnswerResponse(

	@field:SerializedName("data")
	val data: DataCheckAccountRecoveryAnswer? = null,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int
)

data class DataCheckAccountRecoveryAnswer(

	@field:SerializedName("isValid")
	val isValid: Boolean
)
