package com.dr.jjsembako.core.utils

import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.data.remote.response.category.DataCategory
import java.util.Locale

object DataMapper {
    fun ListDataCategoryToListFilterOption(data: List<DataCategory>): List<FilterOption> {
        return data.map {
            FilterOption(
                name = it.title.replaceFirstChar { it2 ->
                    if (it2.isLowerCase()) it2.titlecase(Locale.getDefault()) else it2.toString()
                },
                value = it.title
            )
        }
    }
}