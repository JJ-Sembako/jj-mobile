package com.dr.jjsembako.core.data.model

import com.dr.jjsembako.core.data.remote.response.product.DataProduct

data class SelectPNRItem(
    val id: String,
    val amount: Int,
    val actualAmount: Int,
    val selledPrice: Long,
    val status: Int,
    val product: DataProduct,
    val amountSelected: Int = 0,
    val isChosen: Boolean = false
)
