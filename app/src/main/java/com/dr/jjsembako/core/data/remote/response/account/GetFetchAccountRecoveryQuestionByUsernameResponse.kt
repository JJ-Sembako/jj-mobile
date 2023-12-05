package com.dr.jjsembako.core.data.remote.response.account

import com.google.gson.annotations.SerializedName

data class GetFetchAccountRecoveryQuestionByUsernameResponse(

	@field:SerializedName("data")
	val data: DataAccountRecoveryQuestion? = null,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int
)

data class DataAccountRecoveryQuestion(

	@field:SerializedName("question")
	val question: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("role")
	val role: String
)
