package com.dr.jjsembako.performa.domain.usecase.performance

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.performa.domain.model.Omzet
import kotlinx.coroutines.flow.Flow

interface FetchOmzetMonthlyUseCase {

    suspend fun fetchOmzetMonthly(month: Int, year: Int): Flow<Resource<out Omzet?>>

}