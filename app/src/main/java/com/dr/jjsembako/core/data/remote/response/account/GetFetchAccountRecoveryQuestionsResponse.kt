package com.dr.jjsembako.core.data.remote.response.account

import com.google.gson.annotations.SerializedName

data class GetFetchAccountRecoveryQuestionsResponse(

	@field:SerializedName("data")
	val data: List<DataAccountRecoveryQuestion>? = null,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int
)
