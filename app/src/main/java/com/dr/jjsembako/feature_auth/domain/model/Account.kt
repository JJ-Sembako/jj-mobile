package com.dr.jjsembako.feature_auth.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Account (
    val username: String,
    val role: String
): Parcelable
