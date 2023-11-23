package com.dr.jjsembako.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")

    object BuatPesanan : Screen("buatpesanan")
    object Gudang : Screen("gudang")
    object Pelanggan : Screen("pelanggan")
    object Riwayat : Screen("riwayat")
    object Performa : Screen("performa")
    object Pengaturan : Screen("pengaturan")
}