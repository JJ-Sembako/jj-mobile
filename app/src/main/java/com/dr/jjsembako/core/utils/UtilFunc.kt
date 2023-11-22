package com.dr.jjsembako.core.utils

/**
 * Toolkits Pengecekan Autentikasi
 */
fun isValidUsername(username: String): Boolean {
    val regex = Regex("^[a-zA-Z]+[a-zA-Z0-9_-]{4,}\$")
    return username.isNotEmpty() && regex.matches(username)
}

fun isValidPassword(password: String): Boolean {
    return password.length >= 8
}