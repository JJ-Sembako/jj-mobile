package com.dr.jjsembako.core.data.model

import com.dr.jjsembako.core.data.remote.response.product.DataProduct

data class SelectReturItem(
    val id: String,
    val amount: Int,
    val status: Int,
    val oldSelledPrice: Long,
    val selledPrice: Long,
    val createdAt: String,
    val updatedAt: String,
    val returedProduct: DataProduct,
    val returnedProduct: DataProduct,
    val amountSelected: Int = 0,
    val isChosen: Boolean = false
)
