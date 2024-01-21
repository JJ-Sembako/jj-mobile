package com.dr.jjsembako.core.utils

import com.dr.jjsembako.ProductOrderStore
import com.dr.jjsembako.core.data.model.DataProductOrder
import com.dr.jjsembako.core.data.model.FilterOption

object DataMapper {
    fun mapListDataCategoryToListFilterOption(data: List<String?>?): List<FilterOption?> {
        return if (data.isNullOrEmpty()) {
            emptyList()
        } else {
            data.mapNotNull { category ->
                if (!category.isNullOrEmpty()) {
                    FilterOption(
                        name = category,
                        value = category
                    )
                } else {
                    null
                }
            }
        }
    }

    fun mapListDataProductOrderToListProductOrderStore(data: List<DataProductOrder?>) : List<ProductOrderStore> {
        return if(data.isEmpty()){
            emptyList()
        } else {
            data.mapNotNull { product ->
                if(product != null && product.isChosen && product.orderQty != 0){
                    val productOrderStoreBuilder = ProductOrderStore.newBuilder()
                    productOrderStoreBuilder
                        .setId(product.id)
                        .setOrderQty(product.orderQty)
                        .setOrderPrice(product.orderPrice)
                        .orderTotalPrice = product.orderTotalPrice

                    val productOrderStore = productOrderStoreBuilder.build()

                    // return value
                    productOrderStore

                } else null
            }
        }
    }
}