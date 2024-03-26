package com.dr.jjsembako.core.data.dummy

import com.dr.jjsembako.core.data.model.DataProductOrder
import com.dr.jjsembako.core.data.model.SelectPNRItem
import com.dr.jjsembako.core.data.model.SelectSubstituteItem
import com.dr.jjsembako.core.data.remote.response.customer.Customer
import com.dr.jjsembako.core.data.remote.response.order.Account
import com.dr.jjsembako.core.data.remote.response.order.CanceledData
import com.dr.jjsembako.core.data.remote.response.order.DataDetailOrder
import com.dr.jjsembako.core.data.remote.response.order.OrderToProductsItem
import com.dr.jjsembako.core.data.remote.response.order.ReturData
import com.dr.jjsembako.core.data.remote.response.product.Product
import com.dr.jjsembako.feature_history.domain.model.DataOrderHistoryCard
import com.dr.jjsembako.feature_history.domain.model.DataOrderTimestamps

val dataOrderDataItem = DataOrderHistoryCard(
    id = "123",
    invoice = "INV/20240131/13951",
    orderStatus = 0,
    paymentStatus = 0,
    totalPrice = 1_000_000L,
    createdAt = "2024-01-23T12:25:00.000Z",
    account = Account(
        id = "s123",
        role = "sales",
        username = "dimassales"
    ),
    customer = Customer(
        id = "bd5da2d9-a0bd-4f0f-b730-c88ca915e9b6",
        name = "Narji Choi",
        shopName = "Abadi Rahadi",
        address = "Jl. Veteran No.16, Magero, Sragen Tengah, Kec. Sragen, Kabupaten Sragen, Jawa Tengah 57211",
        gmapsLink = "https://maps.app.goo.gl/7kj9ocXFyNB6SspT8",
        phoneNumber = "085225213456"
    )
)

val dataOrderTimestamps = DataOrderTimestamps(
    createdAt = "2024-01-23T12:25:00.000Z",
    updatedAt = "2024-02-04T23:16:57.000Z",
    deliverAt = "2024-02-04T23:16:57.000Z",
    finishedAt = "2024-02-04T23:16:57.000Z"
)

val dataOrderToProductsItem = listOf(
    OrderToProductsItem(
        id = "9be23a7a-ef7a-4299-92af-29ff955d9cf2",
        amount = 3,
        actualAmount = 1,
        selledPrice = 26000L,
        status = 1,
        createdAt = "2023-12-24T01:38:25.095Z",
        updatedAt = "2024-02-04T23:54:00.986Z",
        product = Product(
            id = "9be23a7a-ef7a-4299-92af-29ff955d9cf2",
            name = "Tepung Norin",
            stockInPcs = 90,
            stockInUnit = 15,
            stockInPcsRemaining = 0,
            standardPrice = 23000L,
            amountPerUnit = 6,
            image = "http://54.251.20.182/img/default.png",
            unit = "Karung",
            category = "Tepung"
        )
    ),
    OrderToProductsItem(
        id = "aeb8bbef-1b53-4b96-ad39-904aecca0852",
        amount = 3,
        actualAmount = 2,
        selledPrice = 45000L,
        status = 2,
        createdAt = "2023-12-24T01:38:25.095Z",
        updatedAt = "2024-02-04T23:54:00.986Z",
        product = Product(
            id = "aeb8bbef-1b53-4b96-ad39-904aecca0852",
            name = "Air Cahaya",
            stockInPcs = 168,
            stockInUnit = 7,
            stockInPcsRemaining = 0,
            standardPrice = 42000L,
            amountPerUnit = 24,
            image = "http://54.251.20.182/img/default.png",
            unit = "Karton",
            category = "Air Mineral"
        )
    )
)

val dataRetur = listOf(
    ReturData(
        id = "60968e57-6b09-4e6d-bc41-54f27650aa30",
        amount = 2,
        status = 0,
        oldSelledPrice = 26000L,
        selledPrice = 26000L,
        createdAt = "2024-02-04T23:54:00.976Z",
        updatedAt = "2024-02-04T23:54:00.976Z",
        returedProduct = Product(
            id = "9be23a7a-ef7a-4299-92af-29ff955d9cf2",
            name = "Tepung Norin",
            stockInPcs = 90,
            stockInUnit = 15,
            stockInPcsRemaining = 0,
            standardPrice = 23000L,
            amountPerUnit = 6,
            image = "http://54.251.20.182/img/default.png",
            unit = "Karung",
            category = "Tepung"
        ),
        returnedProduct = Product(
            id = "9be23a7a-ef7a-4299-92af-29ff955d9cf2",
            name = "Tepung Norin",
            stockInPcs = 90,
            stockInUnit = 15,
            stockInPcsRemaining = 0,
            standardPrice = 23000L,
            amountPerUnit = 6,
            image = "http://54.251.20.182/img/default.png",
            unit = "Karung",
            category = "Tepung"
        )
    )
)

val dataCanceled = listOf(
    CanceledData(
        id = "aeb8bbef-1b53-4b96-ad39-904aecca0852",
        amount = 1,
        status = 0,
        selledPrice = 45000L,
        createdAt = "2024-02-04T23:07:27.000Z",
        updatedAt = "2024-02-04T23:07:27.000Z",
        product = Product(
            id = "aeb8bbef-1b53-4b96-ad39-904aecca0852",
            name = "Air Cahaya",
            stockInPcs = 168,
            stockInUnit = 7,
            stockInPcsRemaining = 0,
            standardPrice = 42000L,
            amountPerUnit = 24,
            image = "http://54.251.20.182/img/default.png",
            unit = "Karton",
            category = "Air Mineral"
        )
    )
)

val dataProductOrder = DataProductOrder(
    id = "bc3bbd9e",
    name = "Air Cahaya",
    image = "",
    category = "Air Mineral",
    unit = "karton",
    standardPrice = 55000,
    amountPerUnit = 16,
    stockInPcs = 256,
    stockInUnit = 16,
    stockInPcsRemaining = 0
)

val dataDetailOrder = DataDetailOrder(
    id = "123",
    invoice = "INV/20240131/13951",
    orderStatus = 0,
    paymentStatus = 0,
    totalPrice = 1_000_000L,
    actualTotalPrice = 1_000_000L,
    createdAt = "2024-01-23T12:25:00.000Z",
    updatedAt = "2024-02-04T23:16:57.000Z",
    deliverAt = "2024-02-04T23:16:57.000Z",
    finishedAt = "2024-02-04T23:16:57.000Z",
    account = Account(
        id = "s123",
        role = "sales",
        username = "dimassales"
    ),
    customer = Customer(
        id = "bd5da2d9-a0bd-4f0f-b730-c88ca915e9b6",
        name = "Narji Choi",
        shopName = "Abadi Rahadi",
        address = "Jl. Veteran No.16, Magero, Sragen Tengah, Kec. Sragen, Kabupaten Sragen, Jawa Tengah 57211",
        gmapsLink = "https://maps.app.goo.gl/7kj9ocXFyNB6SspT8",
        phoneNumber = "085225213456"
    ),
    orderToProducts = listOf(
        OrderToProductsItem(
            id = "9be23a7a-ef7a-4299-92af-29ff955d9cf2",
            amount = 3,
            actualAmount = 1,
            selledPrice = 26000L,
            status = 1,
            createdAt = "2023-12-24T01:38:25.095Z",
            updatedAt = "2024-02-04T23:54:00.986Z",
            product = Product(
                id = "9be23a7a-ef7a-4299-92af-29ff955d9cf2",
                name = "Tepung Norin",
                stockInPcs = 90,
                stockInUnit = 15,
                stockInPcsRemaining = 0,
                standardPrice = 23000L,
                amountPerUnit = 6,
                image = "http://54.251.20.182/img/default.png",
                unit = "Karung",
                category = "Tepung"
            )
        ),
        OrderToProductsItem(
            id = "aeb8bbef-1b53-4b96-ad39-904aecca0852",
            amount = 3,
            actualAmount = 2,
            selledPrice = 45000L,
            status = 2,
            createdAt = "2023-12-24T01:38:25.095Z",
            updatedAt = "2024-02-04T23:54:00.986Z",
            product = Product(
                id = "aeb8bbef-1b53-4b96-ad39-904aecca0852",
                name = "Air Cahaya",
                stockInPcs = 168,
                stockInUnit = 7,
                stockInPcsRemaining = 0,
                standardPrice = 42000L,
                amountPerUnit = 24,
                image = "http://54.251.20.182/img/default.png",
                unit = "Karton",
                category = "Air Mineral"
            )
        )
    ),
    canceled = null,
    retur = null
)

val dataSelectPNRItem = SelectPNRItem(
    id = "9be23a7a-ef7a-4299-92af-29ff955d9cf2",
    amount = 3,
    actualAmount = 1,
    selledPrice = 26000L,
    status = 1,
    product = Product(
        id = "9be23a7a-ef7a-4299-92af-29ff955d9cf2",
        name = "Tepung Norin",
        stockInPcs = 90,
        stockInUnit = 15,
        stockInPcsRemaining = 0,
        standardPrice = 23000L,
        amountPerUnit = 6,
        image = "http://54.251.20.182/img/default.png",
        unit = "Karung",
        category = "Tepung"
    )
)

val dataSelectSubstituteItem = SelectSubstituteItem(
    id = "9be23a7a-ef7a-4299-92af-29ff955d9cf2",
    name = "Tepung Norin",
    stockInPcs = 90,
    stockInUnit = 15,
    stockInPcsRemaining = 0,
    standardPrice = 23000L,
    amountPerUnit = 6,
    image = "http://54.251.20.182/img/default.png",
    unit = "Karung",
    category = "Tepung"
)