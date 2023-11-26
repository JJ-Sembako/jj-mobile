package com.dr.jjsembako.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")

    object PengecekanUsername: Screen("pemulihan/cek-username")
    object PertanyaanPemulihan: Screen("pemulihan/pertanyaan")
    object PemulihanKataSandi: Screen("pemulihan/ganti-kata-sandi")

    object BuatPesanan : Screen("buatpesanan")
    object Gudang : Screen("gudang")
    object Pelanggan : Screen("pelanggan")
    object Riwayat : Screen("riwayat")
    object Performa : Screen("performa")

    object Pengaturan : Screen("pengaturan")
    object GantiKataSandi : Screen("pengaturan/ganti-kata-sandi")
}