package com.dr.jjsembako.core.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
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

fun isValidNewPassword(newPassword: String, confNewPassword: String): Boolean {
    return newPassword == confNewPassword
}

fun isDifferentPassword(oldPassword: String, newPassword: String): Boolean {
    return oldPassword != newPassword
}

/**
 * Toolkits Transform Objek
 */
fun formatRupiah(total: Long): String {
    val formatter = NumberFormat.getInstance(Locale("id", "ID"))
    return "Rp${formatter.format(total)},00"
}

fun formatTotal(total: Int): String {
    val formatter = NumberFormat.getInstance(Locale("id", "ID"))
    return formatter.format(total)
}

/**
 * Toolkits Pemulihan
 */
fun isValidAnswer(answer: String): Boolean{
    return answer.length >= 3
}

/**
 * Toolkits Pelanggan
 */
fun isValidPhoneNumber(phone: String): Boolean{
    val regex = Regex("^(\\+62|62)?[\\s-]?0?8[1-9]{1}\\d{1}[\\s-]?\\d{4}[\\s-]?\\d{2,5}\$")
    return phone.isNotEmpty() && regex.matches(phone)
}
fun isValidLinkMaps(link: String): Boolean{
    val regexGmaps = Regex("(https:\\/\\/)goo.gl\\/maps\\/.*|(https:\\/\\/)www.google.(co.id|com)\\/maps\\/place\\/.*|(https:\\/\\/)maps.app.goo.gl\\/.*")
    val regexAppleMaps = Regex("(https:\\/\\/)maps.apple.com\\/.*|(https:\\/\\/)maps\\.app\\.goo\\.gl\\/.*\n")
    return if(link.isEmpty()) false
    else regexGmaps.matches(link) || regexAppleMaps.matches(link)
}
fun convertToChatWA(phone: String): String {
    // Hapus semua karakter yang bukan digit dari nomor telepon
    val cleanedPhoneNumber = phone.replace(Regex("[^\\d]"), "")

    // Periksa apakah nomor telepon dimulai dengan 0, jika iya, hilangkan
    val formattedPhoneNumber = if (cleanedPhoneNumber.startsWith("0")) {
        cleanedPhoneNumber.substring(1)
    } else {
        cleanedPhoneNumber
    }

    // Membuat link
    val waLink = when {
        formattedPhoneNumber.startsWith("62") -> "https://wa.me/$formattedPhoneNumber"
        formattedPhoneNumber.startsWith("+62") -> "https://wa.me/$formattedPhoneNumber"
        formattedPhoneNumber.startsWith("0") -> "https://wa.me/62$formattedPhoneNumber"
        else -> "https://wa.me/$formattedPhoneNumber"
    }

    return waLink
}

fun openMaps(context: Context, url: String) {
    Log.e("Maps Link", "URL IS: $url")
    val urlIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(url)
    )
    context.startActivity(urlIntent)
}

fun chatWA(context: Context, url: String) {
    Log.e("Chat WA", "URL IS: $url")
    val urlIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(convertToChatWA(url))
    )
    context.startActivity(urlIntent)
}

fun call(context: Context, uri: String) {
    Log.e("Call", "URI IS: $uri")
    val callIntent = Intent(
        Intent.ACTION_CALL,
        Uri.parse("tel: $uri")
    )
    context.startActivity(callIntent)
}