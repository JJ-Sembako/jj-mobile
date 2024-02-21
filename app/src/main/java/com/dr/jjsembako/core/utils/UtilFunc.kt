package com.dr.jjsembako.core.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.runtime.MutableState
import com.dr.jjsembako.core.data.remote.response.performance.OmzetData
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Toolkits Sistem
 */
fun getAppVersion(context: Context): String {
    return try {
        val packageManager = context.packageManager
        val packageName = context.packageName
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            packageManager.getPackageInfo(packageName, 0)
        }
        packageInfo.versionName
    } catch (e: Exception) {
        "x.x.x"
    }
}

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

fun formatShortRupiah(total: Long): String {
    val formatter = NumberFormat.getInstance(Locale("id", "ID"))
    formatter.maximumFractionDigits = 1
    val absTotal = Math.abs(total)
    return when {
        absTotal >= 1_000_000_000 -> {
            val miliar = absTotal / 1_000_000_000.0
            formatter.format(miliar).replace(",", ".") + "M"
        }
        absTotal >= 1_000_000 -> {
            val juta = absTotal / 1_000_000.0
            formatter.format(juta).replace(",", ".") + "Jt"
        }
        absTotal >= 1_000 -> {
            val ribu = absTotal / 1_000.0
            formatter.format(ribu).replace(",", ".") + "Rb"
        }
        else -> {
            formatter.format(total)
        }
    }
}

/**
 * Toolkits Pemulihan
 */
fun isValidAnswer(answer: String): Boolean {
    return answer.length >= 3
}

/**
 * Toolkits Pelanggan
 */
fun isValidPhoneNumber(phone: String): Boolean {
    val regex = Regex("^(\\+62|62)?[\\s-]?0?8[1-9]{1}\\d{1}[\\s-]?\\d{4}[\\s-]?\\d{2,5}\$")
    return phone.isNotEmpty() && regex.matches(phone)
}

fun isValidLinkMaps(link: String): Boolean {
    val regexGmaps =
        Regex("(https:\\/\\/)goo.gl\\/maps\\/.*|(https:\\/\\/)www.google.(co.id|com)\\/maps\\/place\\/.*|(https:\\/\\/)maps.app.goo.gl\\/.*")
    val regexAppleMaps =
        Regex("(https:\\/\\/)maps.apple.com\\/.*|(https:\\/\\/)maps\\.app\\.goo\\.gl\\/.*\n")
    return if (link.isEmpty()) false
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
        Intent.ACTION_DIAL,
        Uri.parse("tel: $uri")
    )
    context.startActivity(callIntent)
}

/**
 * Toolkits Riwayat
 */
fun initializeDateValues(fromDate: MutableState<String>, untilDate: MutableState<String>) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        fromDate.value = LocalDate.now().withDayOfMonth(1)
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        untilDate.value =
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    } else {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfMonth = calendar.time
        fromDate.value =
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(firstDayOfMonth)
        untilDate.value =
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    }
}

fun formatDateString(dateString: String): String {
    val parts = dateString.split("-")
    return if (parts.size == 3) {
        "${parts[2]}-${parts[1]}-${parts[0]}"
    } else {
        dateString
    }
}

fun convertMillisToDate(millis: Long): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val selectedLocalDate = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC"))
            .toLocalDate()
        selectedLocalDate.format(formatter)
    } else {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        formatter.format(Date(millis))
    }
}

fun convertDateStringToCalendar(
    fromDate: MutableState<String>,
    untilDate: MutableState<String>,
    calendarFromDate: Calendar,
    calendarUntilDate: Calendar,
) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val fromDateLocalDate =
                LocalDate.parse(fromDate.value, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            val untilDateLocalDate =
                LocalDate.parse(untilDate.value, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            calendarFromDate.time =
                Date.from(fromDateLocalDate.atStartOfDay(ZoneId.of("UTC")).toInstant())
            calendarUntilDate.time =
                Date.from(untilDateLocalDate.atStartOfDay(ZoneId.of("UTC")).toInstant())
        } else {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            dateFormat.isLenient = false
            calendarFromDate.time = dateFormat.parse(fromDate.value)!!
            calendarUntilDate.time = dateFormat.parse(untilDate.value)!!
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
fun convertTimestampToArray(timestamp: String): Array<String> {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val outputTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    try {
        val date = inputFormat.parse(timestamp)
        val formattedDate = outputDateFormat.format(date)
        val formattedTime = outputTimeFormat.format(date)

        return arrayOf(formattedDate, formattedTime)
    } catch (e: ParseException) {
        e.printStackTrace()
        Log.e("toDateArray", "Error converting date:", e)
        return arrayOf("ERR CONVERT DATE", "ERR CONVERT TIME")
    }
}

/**
 * Toolkits Performa
 */
fun getCurrentYearMonthInGmt7(): List<Int> {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"))
    return listOf(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
}

fun labelPerformance(data: List<OmzetData?>?, month: List<String>): List<String> {
    if (data.isNullOrEmpty()) return emptyList()
    else {
        return data.mapNotNull { omzetData ->
            val monthIndex = omzetData?.month?.minus(1) ?: -1
            if (monthIndex in 0 until month.size) {
                "${month[monthIndex]} ${omzetData?.year}"
            } else {
                null
            }
        }
    }
}