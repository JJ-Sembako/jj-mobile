package com.dr.jjsembako.core.data.remote.response.performance

import com.google.gson.annotations.SerializedName

data class GetFetchOmzetResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("data")
	val data: List<OmzetData?>? = null
)

data class OmzetData(

	@field:SerializedName("year")
	val year: Int,

	@field:SerializedName("month")
	val month: Int,

	@field:SerializedName("omzet")
	val omzet: Int
)
