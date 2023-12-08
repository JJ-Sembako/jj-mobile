package com.dr.jjsembako.feature_setting.domain.usecase

import com.dr.jjsembako.core.common.Resource
import com.dr.jjsembako.core.data.remote.response.account.PatchHandleUpdateSelfPasswordResponse
import com.dr.jjsembako.feature_setting.domain.repository.ISettingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChangePasswordInteractor @Inject constructor(private val settingRepository: ISettingRepository) :
    ChangePasswordUseCase {
    override suspend fun handleUpdateSelfPassword(
        oldPassword: String,
        newPassword: String,
        confNewPassword: String
    ): Flow<Resource<out PatchHandleUpdateSelfPasswordResponse>> =
        settingRepository.handleUpdateSelfPassword(
            oldPassword,
            newPassword,
            confNewPassword
        )
}