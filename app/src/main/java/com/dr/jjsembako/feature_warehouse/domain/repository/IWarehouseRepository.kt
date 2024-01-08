package com.dr.jjsembako.feature_warehouse.domain.repository

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.category.DataCategory
import kotlinx.coroutines.flow.Flow

interface IWarehouseRepository {
    suspend fun fetchCategories(): Flow<Resource<out List<DataCategory?>>>
}