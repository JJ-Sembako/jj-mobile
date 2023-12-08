package com.dr.jjsembako.feature_setting.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.DataAccountRecovery
import kotlinx.coroutines.flow.Flow

interface GetDataAccountRecoveryUSeCase {

    suspend fun fetchAccountRecovery(): Flow<Resource<out DataAccountRecovery?>>

}