package com.dr.jjsembako.core.data.model

import com.dr.jjsembako.core.data.remote.response.product.DataProduct

data class SelectCanceledItem(
    val id: String,
    val amount: Int,
    val status: Int,
    val selledPrice: Long,
    val createdAt: String,
    val updatedAt: String,
    val product: DataProduct,
    val amountSelected: Int = 0,
    val isChosen: Boolean = false
)
