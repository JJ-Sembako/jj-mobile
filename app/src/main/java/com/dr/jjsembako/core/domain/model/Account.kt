package com.dr.jjsembako.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Account (
    val username: String,
    val role: String
): Parcelable