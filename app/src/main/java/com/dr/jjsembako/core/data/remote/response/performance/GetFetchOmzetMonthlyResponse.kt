package com.dr.jjsembako.core.data.remote.response.performance

import com.google.gson.annotations.SerializedName

data class GetFetchOmzetMonthlyResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("data")
	val data: Omzet? = null
)
