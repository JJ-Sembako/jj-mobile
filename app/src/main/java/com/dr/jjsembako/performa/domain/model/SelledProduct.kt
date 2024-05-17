package com.dr.jjsembako.performa.domain.model

import com.google.gson.annotations.SerializedName

data class SelledProduct(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("image")
    val image: String = "",

    @field:SerializedName("unit")
    val unit: String,

    @field:SerializedName("num_of_selled")
    val numOfSelled: Int
)