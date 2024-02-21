package com.dr.jjsembako

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.TokenViewModel
import com.dr.jjsembako.core.utils.call
import com.dr.jjsembako.core.utils.chatWA
import com.dr.jjsembako.core.utils.getAppVersion
import com.dr.jjsembako.core.utils.openMaps
import com.dr.jjsembako.feature_auth.presentation.check_username.PengecekanUsernameScreen
import com.dr.jjsembako.feature_auth.presentation.login.LoginScreen
import com.dr.jjsembako.feature_auth.presentation.password_recovery.PemulihanKataSandiScreen
import com.dr.jjsembako.feature_auth.presentation.recovery_question.PertanyaanPemulihanScreen
import com.dr.jjsembako.feature_customer.presentation.add.TambahPelangganScreen
import com.dr.jjsembako.feature_customer.presentation.detail.DetailPelangganScreen
import com.dr.jjsembako.feature_customer.presentation.edit.EditPelangganScreen
import com.dr.jjsembako.feature_customer.presentation.history_order.PesananPelangganScreen
import com.dr.jjsembako.feature_customer.presentation.list.PelangganScreen
import com.dr.jjsembako.feature_history.presentation.add_product_order.TambahBarangPesananScreen
import com.dr.jjsembako.feature_history.presentation.detail.DetailTransaksiScreen
import com.dr.jjsembako.feature_history.presentation.edit_product_order.EditBarangPesananScreen
import com.dr.jjsembako.feature_history.presentation.list.RiwayatScreen
import com.dr.jjsembako.feature_history.presentation.potong_nota.create.PotongNotaScreen
import com.dr.jjsembako.feature_history.presentation.potong_nota.select_product.PilihBarangPotongNotaScreen
import com.dr.jjsembako.feature_history.presentation.retur.create.ReturScreen
import com.dr.jjsembako.feature_history.presentation.retur.select_product.PilihBarangReturScreen
import com.dr.jjsembako.feature_history.presentation.retur.select_substitute.PilihPenggantiReturScreen
import com.dr.jjsembako.feature_home.presentation.HomeScreen
import com.dr.jjsembako.feature_order.presentation.create_order.BuatPesananScreen
import com.dr.jjsembako.feature_order.presentation.select_cust.PilihPelangganScreen
import com.dr.jjsembako.feature_order.presentation.select_product.PilihBarangScreen
import com.dr.jjsembako.feature_performance.presentation.performance.PerformaScreen
import com.dr.jjsembako.feature_setting.presentation.change_password.GantiKataSandiScreen
import com.dr.jjsembako.feature_setting.presentation.recovery.PemulihanAkunScreen
import com.dr.jjsembako.feature_setting.presentation.setting.PengaturanScreen
import com.dr.jjsembako.feature_warehouse.presentation.GudangScreen
import com.dr.jjsembako.navigation.Screen

@Composable
fun JJSembakoApp() {
    val tokenViewModel: TokenViewModel = hiltViewModel()
    val token by tokenViewModel.token.collectAsState()
    val username by tokenViewModel.username.collectAsState()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val activity = (LocalLifecycleOwner.current as ComponentActivity)
    val navController = rememberNavController()
    val startDestination = if (token.isEmpty()) Screen.Login.route else Screen.Home.route

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            if (token.isNotEmpty()) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            } else {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToCheckUsername = {
                        navController.navigate(Screen.PengecekanUsername.route) {
                            launchSingleTop = true
                        }
                    },
                    setToken = { newToken ->
                        tokenViewModel.setToken(newToken)
                        tokenViewModel.updateStateToken()
                    },
                    setUsername = { username ->
                        tokenViewModel.setUsername(username)
                        tokenViewModel.updateStateUsername()
                    }
                )
            }
        }

        composable(Screen.PengecekanUsername.route) {
            PengecekanUsernameScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToCheckAnswer = { username ->
                    navController.navigate(Screen.PertanyaanPemulihan.createRoute(username)) {
                        launchSingleTop = true
                    }
                })
        }

        composable(
            route = Screen.PertanyaanPemulihan.route,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) {
            val recoveryUsername = it.arguments?.getString("username") ?: ""
            PertanyaanPemulihanScreen(
                username = recoveryUsername,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToChangePassword = { username ->
                    navController.navigate(Screen.PemulihanKataSandi.createRoute(username)) {
                        launchSingleTop = true
                    }
                })
        }

        composable(
            route = Screen.PemulihanKataSandi.route,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) {
            val recoveryUsername = it.arguments?.getString("username") ?: ""
            PemulihanKataSandiScreen(
                username = recoveryUsername,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                })
        }

        composable(Screen.Home.route) {
            if (token.isEmpty()) {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            } else {
                HomeScreen(
                    username = username,
                    context = context,
                    clipboardManager = clipboardManager,
                    onNavigateToDetail = { id ->
                        navController.navigate(Screen.DetailRiwayat.createRoute(id)) {
                            launchSingleTop = true
                        }
                    },
                    onNavigateToCreateOrder = {
                        navController.navigate(Screen.BuatPesanan.route) {
                            launchSingleTop = true
                        }
                    },
                    onNavigateToWarehouse = {
                        navController.navigate(Screen.Gudang.route) {
                            launchSingleTop = true
                        }
                    },
                    onNavigateToCustomer = {
                        navController.navigate(Screen.Pelanggan.createRoute("")) {
                            launchSingleTop = true
                        }
                    },
                    onNavigateToHistory = {
                        navController.navigate(Screen.Riwayat.route) {
                            launchSingleTop = true
                        }
                    },
                    onNavigateToPerformance = {
                        navController.navigate(Screen.Performa.route) {
                            launchSingleTop = true
                        }
                    },
                    onNavigateToSetting = {
                        navController.navigate(Screen.Pengaturan.route) {
                            launchSingleTop = true
                        }
                    },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                        tokenViewModel.setToken("")
                        tokenViewModel.setUsername("username")
                        tokenViewModel.updateStateToken()
                        tokenViewModel.updateStateUsername()
                    },
                    backHandler = { activity.finish() }
                )
            }
        }

        composable(Screen.BuatPesanan.route) {
            BuatPesananScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSelectCustomer = {
                    navController.navigate(Screen.BuatPesananPilihPelangan.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToSelectProduct = {
                    navController.navigate(Screen.BuatPesananPilihBarang.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToDetailTransaction = { id ->
                    navController.navigate(Screen.DetailRiwayat.createRoute(id)) {
                        popUpTo(Screen.BuatPesanan.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.BuatPesananPilihPelangan.route) {
            PilihPelangganScreen(onNavigateToMainOrderScreen = {
                navController.navigate(Screen.BuatPesanan.route) {
                    popUpTo(Screen.BuatPesanan.route) { inclusive = true }
                }
            })
        }

        composable(Screen.BuatPesananPilihBarang.route) {
            PilihBarangScreen(onNavigateToMainOrderScreen = {
                navController.navigate(Screen.BuatPesanan.route) {
                    popUpTo(Screen.BuatPesanan.route) { inclusive = true }
                }
            })
        }

        composable(Screen.Gudang.route) {
            GudangScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.Pelanggan.route,
            arguments = listOf(navArgument("keyword") {
                type = NavType.StringType
                nullable = true
            })
        ) {
            val keyword = it.arguments?.getString("keyword") ?: ""
            PelangganScreen(
                keyword = keyword,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetailCust = { id, latestKeyword ->
                    navController.navigate(Screen.DetailPelanggan.createRoute(id, latestKeyword)) {
                        launchSingleTop = true
                    }
                },
                onNavigateToAddCust = { latestKeyword ->
                    navController.navigate(Screen.TambahPelanggan.createRoute(latestKeyword)) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Screen.TambahPelanggan.route,
            arguments = listOf(navArgument("keyword") { type = NavType.StringType })
        ) {
            val keyword = it.arguments?.getString("keyword") ?: ""
            TambahPelangganScreen(
                onNavigateToPelangganScreen = {
                    navController.navigate(Screen.Pelanggan.createRoute(keyword)) {
                        popUpTo(Screen.Pelanggan.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                openMaps = { url ->
                    openMaps(context, url)
                })
        }

        composable(
            route = Screen.DetailPelanggan.route,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("keyword") { type = NavType.StringType }
            )
        ) {
            val id = it.arguments?.getString("id") ?: ""
            val keyword = it.arguments?.getString("keyword") ?: ""
            DetailPelangganScreen(
                idCust = id,
                onNavigateBack = {
                    navController.navigate(Screen.Pelanggan.createRoute(keyword)) {
                        popUpTo(Screen.Pelanggan.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToEditCust = {
                    navController.navigate(Screen.EditPelanggan.createRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onNavigateToCustOrder = {
                    navController.navigate(Screen.PesananPelanggan.createRoute(id)) {
                        launchSingleTop = true
                    }
                },
                openMaps = { url -> openMaps(context, url) },
                call = { uri -> call(context, uri) },
                chatWA = { url -> chatWA(context, url) }
            )
        }

        composable(
            route = Screen.EditPelanggan.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("id") ?: ""
            EditPelangganScreen(
                idCust = id,
                onNavigateToDetailCust = { navController.popBackStack() },
                openMaps = { url -> openMaps(context, url) })
        }

        composable(
            route = Screen.PesananPelanggan.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("id") ?: ""
           PesananPelangganScreen(
               idCust = id,
               context = context,
               clipboardManager = clipboardManager,
               onNavigateBack = { navController.popBackStack() },
               onNavigateToDetail = { idOrder ->
                   navController.navigate(Screen.DetailRiwayat.createRoute(idOrder)) {
                       launchSingleTop = true
                   }
               })
        }

        composable(Screen.Riwayat.route) {
            RiwayatScreen(
                context = context,
                clipboardManager = clipboardManager,
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.DetailRiwayat.createRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.DetailRiwayat.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("id") ?: ""
            DetailTransaksiScreen(
                id = id,
                context = context,
                clipboardManager = clipboardManager,
                openMaps = { url -> openMaps(context, url) },
                call = { uri -> call(context, uri) },
                chatWA = { url -> chatWA(context, url) },
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddProductOrder = {
                    navController.navigate(Screen.TambahBarangPesanan.createRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onNavigateToEditProductOrder = {
                    navController.navigate(Screen.EditBarangPesanan.createRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onNavigateToPotongNota = {
                    navController.navigate(Screen.PotongNota.createRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onNavigateToRetur = {
                    navController.navigate(Screen.Retur.createRoute(id)) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Screen.TambahBarangPesanan.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("id") ?: ""
            TambahBarangPesananScreen(
                id = id,
                onNavigateBack = {
                    navController.navigate(Screen.DetailRiwayat.createRoute(id)) {
                        popUpTo(Screen.DetailRiwayat.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Screen.EditBarangPesanan.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("id") ?: ""
            EditBarangPesananScreen(
                id = id,
                onNavigateBack = {
                    navController.navigate(Screen.DetailRiwayat.createRoute(id)) {
                        popUpTo(Screen.DetailRiwayat.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Screen.PotongNota.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("id") ?: ""
            PotongNotaScreen(
                id = id,
                context = context,
                clipboardManager = clipboardManager,
                onNavigateBack = {
                    navController.navigate(Screen.DetailRiwayat.createRoute(id)) {
                        popUpTo(Screen.DetailRiwayat.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onSelectProduct = {
                    navController.navigate(Screen.PotongNotaPilihBarang.createRoute(id)) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Screen.PotongNotaPilihBarang.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("id") ?: ""
            PilihBarangPotongNotaScreen(
                id = id,
                onNavigateBack = {
                    navController.navigate(Screen.PotongNota.createRoute(id)) {
                        popUpTo(Screen.PotongNota.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Screen.Retur.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("id") ?: ""
            ReturScreen(
                id = id,
                context = context,
                clipboardManager = clipboardManager,
                onNavigateBack = {
                    navController.navigate(Screen.DetailRiwayat.createRoute(id)) {
                        popUpTo(Screen.DetailRiwayat.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onSelectProduct = {
                    navController.navigate(Screen.ReturPilihBarang.createRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onSelectSubstitute = {
                    navController.navigate(Screen.ReturPilihPengganti.createRoute(id)) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Screen.ReturPilihBarang.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("id") ?: ""
            PilihBarangReturScreen(
                id = id,
                onNavigateBack = {
                    navController.navigate(Screen.Retur.createRoute(id)) {
                        popUpTo(Screen.Retur.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Screen.ReturPilihPengganti.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("id") ?: ""
            PilihPenggantiReturScreen(
                onNavigateBack = {
                    navController.navigate(Screen.Retur.createRoute(id)) {
                        popUpTo(Screen.Retur.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Performa.route) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                PerformaScreen(onNavigateBack = { navController.popBackStack() })
            }
        }

        composable(Screen.Pengaturan.route) {
            PengaturanScreen(
                username = username,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                    tokenViewModel.setToken("")
                    tokenViewModel.setUsername("username")
                    tokenViewModel.updateStateToken()
                    tokenViewModel.updateStateUsername()
                },
                onNavigateToChangePassword = {
                    navController.navigate(Screen.GantiKataSandi.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToAccountRecovery = {
                    navController.navigate(Screen.PemulihanAkun.route) {
                        launchSingleTop = true
                    }
                },
                getAppVersion = { getAppVersion(context) }
            )
        }

        composable(Screen.GantiKataSandi.route) {
            GantiKataSandiScreen(onNavigateToSetting = {
                navController.navigate(Screen.Pengaturan.route) {
                    popUpTo(Screen.Pengaturan.route) { inclusive = true }
                }
            })
        }

        composable(Screen.PemulihanAkun.route) {
            PemulihanAkunScreen(onNavigateToSetting = {
                navController.navigate(Screen.Pengaturan.route) {
                    popUpTo(Screen.Pengaturan.route) { inclusive = true }
                }
            })
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun JJSembakoAppPreview() {
    JJSembakoTheme {
        JJSembakoApp()
    }
}