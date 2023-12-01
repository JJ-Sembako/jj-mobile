package com.dr.jjsembako.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")

    object PemulihanAkun: Screen("pemulihan")
    object PengecekanUsername: Screen("pemulihan/cek-username")
    object PertanyaanPemulihan: Screen("pemulihan/pertanyaan")
    object PemulihanKataSandi: Screen("pemulihan/ganti-kata-sandi")

    object BuatPesanan : Screen("buatpesanan")
    object Gudang : Screen("gudang")

    object Pelanggan : Screen("pelanggan")
    object TambahPelanggan : Screen("pelanggan/add")
    object DetailPelanggan : Screen("pelanggan/detail/{id}") {
        fun createRoute(id: String) = "pelanggan/detail/$id"
    }
    object EditPelanggan : Screen("pelanggan/edit/{id}") {
        fun createRoute(id: String) = "pelanggan/edit/$id"
    }

    object Riwayat : Screen("riwayat")
    object Performa : Screen("performa")

    object Pengaturan : Screen("pengaturan")
    object GantiKataSandi : Screen("pengaturan/ganti-kata-sandi")
}