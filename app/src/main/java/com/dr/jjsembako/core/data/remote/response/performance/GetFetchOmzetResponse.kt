package com.dr.jjsembako.core.data.remote.response.performance

import com.dr.jjsembako.performa.domain.model.Omzet
import com.google.gson.annotations.SerializedName

data class GetFetchOmzetResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("data")
	val data: List<Omzet?>? = null
)