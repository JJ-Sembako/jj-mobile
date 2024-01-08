package com.dr.jjsembako.core.utils

import com.dr.jjsembako.core.data.model.FilterOption
import java.util.Locale

object DataMapper {
    fun mapListDataCategoryToListFilterOption(data: List<String?>?): List<FilterOption?> {
        return if (data.isNullOrEmpty()) {
            emptyList()
        } else {
            data.mapNotNull { category ->
                category?.replaceFirstChar { word ->
                    if (word.isLowerCase()) word.titlecase(Locale.getDefault()) else word.toString()
                }?.let { result ->
                    FilterOption(
                        name = result,
                        value = category
                    )
                }
            }
        }
    }
}