package com.dr.jjsembako.core.data.remote.response.account

import com.google.gson.annotations.SerializedName

data class GetCheckAccountExistingResponse(

	@field:SerializedName("data")
	val data: DataCheckAccountExisting? = null,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int
)

data class DataCheckAccountExisting(

	@field:SerializedName("isExist")
	val isExist: Boolean
)
