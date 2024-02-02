package com.dr.jjsembako.core.data.remote.response.order

import com.google.gson.annotations.SerializedName

data class PostHandleCreateOrderResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int
)
