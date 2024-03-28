package com.dr.jjsembako.feature_home.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.performance.Omzet
import kotlinx.coroutines.flow.Flow

interface HomeFetchOmzetUseCase {

    suspend fun fetchOmzetMonthly(month: Int, year: Int): Flow<Resource<out Omzet?>>

}