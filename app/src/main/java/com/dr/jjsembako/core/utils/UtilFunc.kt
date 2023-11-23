package com.dr.jjsembako.core.utils

import java.text.NumberFormat
import java.util.Locale

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

/**
 * Toolkits Transform Objek
 */
fun formatRupiah(total: Long): String{
    val formatter = NumberFormat.getInstance(Locale("id", "ID"))
    return "Rp${formatter.format(total)},00"
}

fun formatTotal(total: Int): String{
    val formatter = NumberFormat.getInstance(Locale("id", "ID"))
    return formatter.format(total)
}