package com.dr.jjsembako.core.domain.model

data class Customer(
    val id: String,
    val name: String,
    val shopName: String,
    val address: String,
    val gmapsLink: String,
    val phoneNumber: String,
    val debt: Long = 0
)
