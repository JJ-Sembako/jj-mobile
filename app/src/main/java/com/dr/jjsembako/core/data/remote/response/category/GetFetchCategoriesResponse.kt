package com.dr.jjsembako.core.data.remote.response.category

import com.google.gson.annotations.SerializedName

data class GetFetchCategoriesResponse(

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: List<DataCategory?>? = null,

    @field:SerializedName("totalData")
    val totalData: Int? = null,

    @field:SerializedName("count")
    val count: Int? = null
)

data class DataCategory(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("created_at")
    val createdAt: String
)
