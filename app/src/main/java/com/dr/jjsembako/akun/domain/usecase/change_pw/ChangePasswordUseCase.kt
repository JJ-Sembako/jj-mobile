package com.dr.jjsembako.akun.domain.usecase.change_pw

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