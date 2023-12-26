package com.dr.jjsembako.core.data.model

data class Product(
    val id: String,
    val name: String,
    val stock: Int,
    val standardPrice: Long,
    val amountPerUnit: Int,
    val image: String,
    val unit: String,
    val category: String
)
