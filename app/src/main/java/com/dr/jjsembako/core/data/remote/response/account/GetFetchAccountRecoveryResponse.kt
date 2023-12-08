package com.dr.jjsembako.core.data.remote.response.account

import com.google.gson.annotations.SerializedName

data class GetFetchAccountRecoveryResponse(

    @field:SerializedName("data")
	val data: DataAccountRecovery? = null,

    @field:SerializedName("message")
	val message: String,

    @field:SerializedName("statusCode")
	val statusCode: Int
)

data class DataAccountRecovery(

	@field:SerializedName("is_active")
	val isActive: Boolean,

	@field:SerializedName("answer")
	val answer: String? = null,

	@field:SerializedName("idQuestion")
	val idQuestion: String? = null
)
