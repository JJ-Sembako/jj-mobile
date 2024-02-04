package com.dr.jjsembako.core.data.dummy

import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.core.data.remote.response.order.Account
import com.dr.jjsembako.core.data.remote.response.order.OrderDataItem
import com.dr.jjsembako.core.data.remote.response.order.OrderToProductsItem
import com.dr.jjsembako.core.data.remote.response.product.DataProduct

val dataOrderDataItem = OrderDataItem(
    id = "123",
    invoice = "INV-123",
    orderStatus = 0,
    paymentStatus = 0,
    totalPrice = 1_000_000L,
    actualTotalPrice = 1_000_000L,
    createdAt = "2024-01-23T12:25:00.000Z",
    updatedAt = "2024-02-01T14:36:39.000Z",
    deliverAt = null,
    finishedAt = null,
    account = Account(
        id = "s123",
        role = "sales",
        username = "dimassales"
    ),
    customer = DataCustomer(
        id = "bd5da2d9-a0bd-4f0f-b730-c88ca915e9b6",
        name = "Narji Choi",
        shopName = "Abadi Rahadi",
        address = "Jl. Veteran No.16, Magero, Sragen Tengah, Kec. Sragen, Kabupaten Sragen, Jawa Tengah 57211",
        gmapsLink = "https://maps.app.goo.gl/7kj9ocXFyNB6SspT8",
        phoneNumber = "085225213456"
    ),
    orderToProducts = listOf(
        OrderToProductsItem(
            id = "123",
            amount = 2,
            isConfirmed = false,
            selledPrice = 50000L,
            status = 0,
            createdAt = "2024-02-01T14:35:18.978Z",
            updatedAt = "2024-02-03T11:55:18.717Z",
            product = DataProduct(
                id = "123",
                name = "Beras",
                image = "",
                stockInPcs = 240,
                stockInUnit = 15,
                stockInPcsRemaining = 0,
                standardPrice = 42000,
                amountPerUnit = 16,
                unit = "Karton",
                category = "Air Mineral"
            )
        )
    ),
    retur = 0,
    canceled = 0
)