package com.dr.jjsembako.akun.domain.model

import com.google.gson.annotations.SerializedName

data class Account(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("role")
    val role: String,

    @field:SerializedName("username")
    val username: String
)