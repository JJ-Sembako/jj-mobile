package com.dr.jjsembako.core.data.remote.response.performance

import com.dr.jjsembako.performa.domain.model.SelledProduct
import com.google.gson.annotations.SerializedName

data class GetFetchSelledProductResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("data")
    val data: List<SelledProduct?>? = null
)