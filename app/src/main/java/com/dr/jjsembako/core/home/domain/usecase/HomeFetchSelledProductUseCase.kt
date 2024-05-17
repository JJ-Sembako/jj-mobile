package com.dr.jjsembako.core.home.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.performa.domain.model.SelledProduct
import kotlinx.coroutines.flow.Flow

interface HomeFetchSelledProductUseCase {

    suspend fun fetchSelledProductMonthly(
        month: Int,
        year: Int
    ): Flow<Resource<out List<SelledProduct?>?>>

}