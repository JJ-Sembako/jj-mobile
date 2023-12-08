package com.dr.jjsembako.core.data.remote.response.account

import com.google.gson.annotations.SerializedName

data class GetCheckAccountRecoveryActivationResponse(

	@field:SerializedName("data")
	val data: DataCheckAccountRecoveryActivation? = null,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int
)

data class DataCheckAccountRecoveryActivation(

	@field:SerializedName("isActive")
	val isActive: Boolean,

	@field:SerializedName("role")
	val role: String
)
