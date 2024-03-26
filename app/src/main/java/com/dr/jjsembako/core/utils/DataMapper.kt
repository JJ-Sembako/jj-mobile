package com.dr.jjsembako.core.utils

import com.dr.jjsembako.CanceledStore
import com.dr.jjsembako.ProductOrderStore
import com.dr.jjsembako.ReturStore
import com.dr.jjsembako.SubstituteStore
import com.dr.jjsembako.core.data.model.DataProductOrder
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.data.model.OrderProduct
import com.dr.jjsembako.core.data.model.SelectPNRItem
import com.dr.jjsembako.core.data.model.SelectSubstituteItem
import com.dr.jjsembako.core.data.remote.response.order.DetailOrder
import com.dr.jjsembako.core.data.remote.response.order.Order
import com.dr.jjsembako.core.data.remote.response.order.OrderToProductsItem
import com.dr.jjsembako.feature_history.domain.model.DataOrderHistoryCard
import com.dr.jjsembako.feature_history.domain.model.DataOrderTimestamps

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

    fun mapOrderDataItemToDataOrderHistoryCard(data: Order): DataOrderHistoryCard {
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

    fun mapDetailOrderDataToDataOrderHistoryCard(data: DetailOrder): DataOrderHistoryCard {
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

    fun mapDetailOrderDataToDataOrderTimestamps(data: DetailOrder): DataOrderTimestamps {
        return DataOrderTimestamps(
            createdAt = data.createdAt,
            updatedAt = data.updatedAt,
            deliverAt = data.deliverAt,
            finishedAt = data.finishedAt
        )
    }

    fun mapListOrderToProductsItemToListSelectPNRItem(data: List<OrderToProductsItem?>) : List<SelectPNRItem> {
        return if (data.isEmpty()) {
            emptyList()
        } else {
            data.mapNotNull { order ->
                if (order != null) {
                    SelectPNRItem(
                        id = order.id,
                        amount = order.amount,
                        actualAmount = order.actualAmount,
                        selledPrice = order.selledPrice,
                        status = order.status,
                        product = order.product
                    )
                } else null
            }
        }
    }

    fun mapSelectPNRItemToCanceledStore(data: SelectPNRItem): CanceledStore {
        val canceledStoreBuilder = CanceledStore.newBuilder()
        canceledStoreBuilder
            .setId(data.id)
            .setIdProduct(data.product.id)
            .setStatus(data.status)
            .amountSelected = data.amountSelected

        // return value
        return canceledStoreBuilder.build()
    }

    fun mapSelectPNRItemToReturStore(data: SelectPNRItem): ReturStore {
        val returStoreBuilder = ReturStore.newBuilder()
        returStoreBuilder
            .setId(data.id)
            .setIdProduct(data.product.id)
            .setStatus(data.status)
            .amountSelected = data.amountSelected

        // return value
        return returStoreBuilder.build()
    }

    fun mapSelectSubstituteItemToSubstituteStore(data: SelectSubstituteItem): SubstituteStore {
        val substituteStoreBuilder = SubstituteStore.newBuilder()
        substituteStoreBuilder
            .setId(data.id)
            .setSelledPrice(data.selledPrice)

        // return value
        return substituteStoreBuilder.build()
    }
}