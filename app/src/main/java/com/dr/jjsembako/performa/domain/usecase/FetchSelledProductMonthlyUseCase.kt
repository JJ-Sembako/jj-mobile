package com.dr.jjsembako.performa.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.performa.domain.model.SelledProduct
import kotlinx.coroutines.flow.Flow

interface FetchSelledProductMonthlyUseCase {

    suspend fun fetchSelledProductMonthly(
        month: Int,
        year: Int
    ): Flow<Resource<out List<SelledProduct?>?>>

}