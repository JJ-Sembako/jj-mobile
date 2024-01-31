package com.dr.jjsembako.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")

    object PemulihanAkun : Screen("pemulihan")
    object PengecekanUsername : Screen("pemulihan/cek-username")
    object PertanyaanPemulihan : Screen("pemulihan/pertanyaan/{username}") {
        fun createRoute(username: String) = "pemulihan/pertanyaan/$username"
    }

    object PemulihanKataSandi : Screen("pemulihan/ganti-kata-sandi/{username}") {
        fun createRoute(username: String) = "pemulihan/ganti-kata-sandi/$username"
    }

    object BuatPesanan : Screen("buatpesanan")
    object BuatPesananPilihPelangan : Screen("buatpesanan/pilih-pelanggan")
    object BuatPesananPilihBarang : Screen("buatpesanan/pilih-barang")

    object Gudang : Screen("gudang")

    object Pelanggan : Screen("pelanggan/?keyword={keyword}") {
        fun createRoute(keyword: String) = "pelanggan/?keyword=$keyword"
    }

    object TambahPelanggan : Screen("pelanggan/add?keyword={keyword}") {
        fun createRoute(keyword: String) = "pelanggan/add?keyword=$keyword"
    }

    object DetailPelanggan : Screen("pelanggan/detail/{id}?keyword={keyword}") {
        fun createRoute(id: String, keyword: String) = "pelanggan/detail/$id?keyword=$keyword"
    }

    object EditPelanggan : Screen("pelanggan/edit/{id}") {
        fun createRoute(id: String) = "pelanggan/edit/$id"
    }

    object Riwayat : Screen("riwayat")
    object DetailRiwayat : Screen("riwayat/detail/{id}") {
        fun createRoute(id: String) = "riwayat/detail/$id"
    }

    object TambahBarangPesanan : Screen("riwayat/detail/tambah/{id}") {
        fun createRoute(id: String) = "riwayat/detail/tambah/$id"
    }

    object EditBarangPesanan : Screen("riwayat/detail/edit/{id}") {
        fun createRoute(id: String) = "riwayat/detail/edit/$id"
    }

    object PotongNota : Screen("riwayat/potong-nota/{id}") {
        fun createRoute(id: String) = "riwayat/potong-nota/$id"
    }

    object PotongNotaPilihBarang : Screen("riwayat/potong-nota/pilih-barang/{id}") {
        fun createRoute(id: String) = "riwayat/potong-nota/pilih-barang/$id"
    }

    object Retur : Screen("riwayat/retur/{id}") {
        fun createRoute(id: String) = "riwayat/retur/$id"
    }

    object ReturPilihBarang : Screen("riwayat/retur/pilih-barang/{id}") {
        fun createRoute(id: String) = "riwayat/retur/pilih-barang/$id"
    }

    object ReturPilihPengganti : Screen("riwayat/retur/pilih-pengganti")

    object Performa : Screen("performa")

    object Pengaturan : Screen("pengaturan")
    object GantiKataSandi : Screen("pengaturan/ganti-kata-sandi")
}