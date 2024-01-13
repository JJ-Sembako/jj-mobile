package com.dr.jjsembako.core.utils

import com.dr.jjsembako.core.data.model.DataCustomerOrder
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer

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

    fun mapListCustDataToListCustDataOrder(data: List<DataCustomer?>?): List<DataCustomerOrder> {
        return if (data.isNullOrEmpty()) {
            emptyList()
        } else {
            data.mapNotNull { cust ->
                if (cust != null) {
                    DataCustomerOrder(
                        id = cust.id,
                        name = cust.name,
                        shopName = cust.shopName,
                        address = cust.address,
                        gmapsLink = cust.gmapsLink,
                        phoneNumber = cust.phoneNumber,
                        debt = cust.debt,
                        isChosen = false
                    )
                } else {
                    null
                }
            }
        }
    }
}