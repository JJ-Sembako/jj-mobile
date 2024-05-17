package com.dr.jjsembako.pelanggan.domain.model

import com.google.gson.annotations.SerializedName

data class Customer(

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
    val debt: Long? = 0L
)