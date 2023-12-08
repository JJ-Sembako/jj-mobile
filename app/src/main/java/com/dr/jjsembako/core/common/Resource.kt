package com.dr.jjsembako.core.common

sealed class Resource<T>(val data: T? = null, val message: String? = null, val status: Int? = null) {
    class Success<T>(data: T, message: String, status: Int) : Resource<T>(data, message, status)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, status: Int, data: T? = null) : Resource<T>(data, message, status)
}