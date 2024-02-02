package com.dr.jjsembako.core.data.remote.response.order

import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.google.gson.annotations.SerializedName

data class GetFetchOrderResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("data")
	val data: DetailOrderData? = null
)

data class DetailOrderData(

	@field:SerializedName("actual_total_price")
	val actualTotalPrice: Int? = null,

	@field:SerializedName("orderToProducts")
	val orderToProducts: List<OrderToProductsItem2?>? = null,

	@field:SerializedName("total_price")
	val totalPrice: Int? = null,

	@field:SerializedName("finished_at")
	val finishedAt: Any? = null,

	@field:SerializedName("payment_status")
	val paymentStatus: Int? = null,

	@field:SerializedName("retur")
	val retur: List<Any?>? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("deliver_at")
	val deliverAt: Any? = null,

	@field:SerializedName("order_status")
	val orderStatus: Int? = null,

	@field:SerializedName("canceled")
	val canceled: List<Any?>? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("invoice")
	val invoice: String? = null,

	@field:SerializedName("account")
	val account: Account? = null,

	@field:SerializedName("customer")
	val customer: DataCustomer ? = null
)

data class OrderToProductsItem2(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("is_confirmed")
	val isConfirmed: Boolean? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("selled_price")
	val selledPrice: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
