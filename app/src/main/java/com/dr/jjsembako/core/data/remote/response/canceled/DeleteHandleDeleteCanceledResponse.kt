package com.dr.jjsembako.core.data.remote.response.canceled

import com.google.gson.annotations.SerializedName

data class DeleteHandleDeleteCanceledResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int
)
