package com.dr.jjsembako.core.utils

import com.dr.jjsembako.ProductOrderStore
import com.dr.jjsembako.core.data.model.DataProductOrder
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.data.model.OrderProduct
import com.dr.jjsembako.core.data.remote.response.order.DetailOrderData
import com.dr.jjsembako.core.data.remote.response.order.OrderDataItem
import com.dr.jjsembako.feature_history.domain.DataOrderHistoryCard

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

    fun mapListDataProductOrderToListProductOrderStore(data: List<DataProductOrder?>): List<ProductOrderStore> {
        return if (data.isEmpty()) {
            emptyList()
        } else {
            data.mapNotNull { product ->
                if (product != null && product.isChosen && product.orderQty != 0) {
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

    fun mapListDataProductOrderStoreToListOrderProduct(data: List<ProductOrderStore?>): List<OrderProduct> {
        return if (data.isEmpty()) {
            emptyList()
        } else {
            data.mapNotNull { product ->
                if (product != null && product.orderQty != 0) {
                    OrderProduct(
                        id = product.id,
                        amountInUnit = product.orderQty,
                        pricePerUnitL = product.orderPrice
                    )
                } else null
            }
        }
    }

    fun mapOrderDataItemToDataOrderHistoryCard(data: OrderDataItem): DataOrderHistoryCard {
        return DataOrderHistoryCard(
            id = data.id,
            invoice = data.invoice,
            orderStatus = data.orderStatus,
            paymentStatus = data.paymentStatus,
            totalPrice = data.totalPrice,
            createdAt = data.createdAt,
            account = data.account,
            customer = data.customer
        )
    }

    fun mapDetailOrderDataToDataOrderHistoryCard(data: DetailOrderData): DataOrderHistoryCard {
        return DataOrderHistoryCard(
            id = data.id,
            invoice = data.invoice,
            orderStatus = data.orderStatus,
            paymentStatus = data.paymentStatus,
            totalPrice = data.totalPrice,
            createdAt = data.createdAt,
            account = data.account,
            customer = data.customer
        )
    }
}