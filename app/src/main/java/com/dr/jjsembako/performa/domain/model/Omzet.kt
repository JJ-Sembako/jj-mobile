package com.dr.jjsembako.performa.domain.model

import com.google.gson.annotations.SerializedName

data class Omzet(

	@field:SerializedName("year")
	val year: Int,

	@field:SerializedName("month")
	val month: Int,

	@field:SerializedName("omzet")
	val omzet: Long
)