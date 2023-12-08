package com.dr.jjsembako.feature_setting.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleUpdateSelfPasswordResponse
import kotlinx.coroutines.flow.Flow

interface ChangePasswordUseCase {

    suspend fun handleUpdateSelfPassword(
        oldPassword: String,
        newPassword: String,
        confNewPassword: String
    ): Flow<Resource<out PatchHandleUpdateSelfPasswordResponse>>

}