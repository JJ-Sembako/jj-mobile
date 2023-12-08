package com.dr.jjsembako.core.data.remote.response.account

import com.google.gson.annotations.SerializedName

data class PostHandleLoginResponse(

	@field:SerializedName("data")
	val data: DataHandleLogin? = null,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int
)

data class DataHandleLogin(

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("token")
	val token: String,

	@field:SerializedName("username")
	val username: String
)
