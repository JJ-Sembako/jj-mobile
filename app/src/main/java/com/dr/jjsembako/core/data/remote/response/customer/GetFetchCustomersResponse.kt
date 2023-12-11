package com.dr.jjsembako.core.data.remote.response.customer

import com.google.gson.annotations.SerializedName

data class GetFetchCustomersResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("data")
    val data: List<DataCustomer>? = null,

    @field:SerializedName("count")
    val count: Int? = null,
)

data class DataCustomer(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("shop_name")
    val shopName: String,

    @field:SerializedName("address")
    val address: String,

    @field:SerializedName("gmaps_link")
    val gmapsLink: String,

    @field:SerializedName("phone_number")
    val phoneNumber: String,

    @field:SerializedName("debt")
    val debt: Long? = 0
)
