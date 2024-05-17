package com.dr.jjsembako.core.home.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.performa.domain.model.Omzet
import kotlinx.coroutines.flow.Flow

interface HomeFetchOmzetUseCase {

    suspend fun fetchOmzetMonthly(month: Int, year: Int): Flow<Resource<out Omzet?>>

}