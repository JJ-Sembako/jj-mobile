package com.dr.jjsembako.core.data.remote.response.performance

import com.google.gson.annotations.SerializedName

data class GetFetchSelledProductResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("data")
    val data: List<SelledData?>? = null
)

data class SelledData(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("image")
    val image: String = "",

    @field:SerializedName("unit")
    val unit: String,

    @field:SerializedName("num_of_selled")
    val numOfSelled: Int
)
