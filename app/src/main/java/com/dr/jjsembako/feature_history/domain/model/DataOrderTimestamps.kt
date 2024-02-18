package com.dr.jjsembako.feature_history.domain.model

data class DataOrderTimestamps(
    val createdAt: String,
    val updatedAt: String,
    val deliverAt: String? = null,
    val finishedAt: String? = null
)
