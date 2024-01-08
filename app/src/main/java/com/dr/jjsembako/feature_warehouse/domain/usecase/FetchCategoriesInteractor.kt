package com.dr.jjsembako.feature_warehouse.domain.usecase

import com.dr.jjsembako.feature_warehouse.domain.repository.IWarehouseRepository
import javax.inject.Inject

class FetchCategoriesInteractor @Inject constructor(private val warehouseRepository: IWarehouseRepository) :
    FetchCategoriesUseCase {
    override suspend fun fetchCategories() = warehouseRepository.fetchCategories()
}