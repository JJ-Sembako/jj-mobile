package com.dr.jjsembako.feature_performance.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.performance.Omzet
import kotlinx.coroutines.flow.Flow

interface FetchOmzetUseCase {

    suspend fun fetchOmzet(): Flow<Resource<out List<Omzet?>?>>

}