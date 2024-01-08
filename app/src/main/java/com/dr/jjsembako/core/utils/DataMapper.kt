package com.dr.jjsembako.core.utils

import com.dr.jjsembako.core.data.model.FilterOption
import java.util.Locale

object DataMapper {
    fun ListDataCategoryToListFilterOption(data: List<String>): List<FilterOption> {
        return data.map {
            FilterOption(
                name = it.replaceFirstChar { it2 ->
                    if (it2.isLowerCase()) it2.titlecase(Locale.getDefault()) else it2.toString()
                },
                value = it
            )
        }
    }
}