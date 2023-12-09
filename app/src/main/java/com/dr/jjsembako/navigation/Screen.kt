package com.dr.jjsembako.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")

    object PemulihanAkun: Screen("pemulihan")
    object PengecekanUsername: Screen("pemulihan/cek-username/{username}") {
        fun createRoute(username: String) = "pemulihan/cek-username/$username"
    }
    object PertanyaanPemulihan: Screen("pemulihan/pertanyaan/{username}") {
        fun createRoute(username: String) = "pemulihan/pertanyaan/$username"
    }
    object PemulihanKataSandi: Screen("pemulihan/ganti-kata-sandi/{username}") {
        fun createRoute(username: String) = "pemulihan/ganti-kata-sandi/$username"
    }

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