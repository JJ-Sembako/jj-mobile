package com.dr.jjsembako.core.data.dummy

import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.core.data.remote.response.order.Account
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
    customer = DataCustomer(
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