package com.dr.jjsembako

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dr.jjsembako.core.utils.call
import com.dr.jjsembako.core.utils.chatWA
import com.dr.jjsembako.core.utils.openMaps
import com.dr.jjsembako.navigation.Screen
import com.dr.jjsembako.ui.feature_auth.check_username.PengecekanUsernameScreen
import com.dr.jjsembako.ui.feature_auth.login.LoginScreen
import com.dr.jjsembako.ui.feature_auth.password_recovery.PemulihanKataSandiScreen
import com.dr.jjsembako.ui.feature_auth.recovery_question.PertanyaanPemulihanScreen
import com.dr.jjsembako.ui.feature_customer.add.TambahPelangganScreen
import com.dr.jjsembako.ui.feature_customer.detail.DetailPelangganScreen
import com.dr.jjsembako.ui.feature_customer.edit.EditPelangganScreen
import com.dr.jjsembako.ui.feature_customer.list.PelangganScreen
import com.dr.jjsembako.ui.feature_home.HomeScreen
import com.dr.jjsembako.ui.feature_setting.change_password.GantiKataSandiScreen
import com.dr.jjsembako.ui.feature_setting.recovery.PemulihanAkunScreen
import com.dr.jjsembako.ui.feature_setting.setting.PengaturanScreen
import com.dr.jjsembako.ui.theme.JJSembakoTheme

@Composable
fun JJSembakoApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val startDestination = Screen.Home.route
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
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
                }
            )
        }

        composable(Screen.PengecekanUsername.route) {
            PengecekanUsernameScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToCheckAnswer = {
                    navController.navigate(Screen.PertanyaanPemulihan.route) {
                        launchSingleTop = true
                    }
                })
        }

        composable(Screen.PertanyaanPemulihan.route) {
            PertanyaanPemulihanScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToChangePassword = {
                    navController.navigate(Screen.PemulihanKataSandi.route) {
                        launchSingleTop = true
                    }
                })
        }

        composable(Screen.PemulihanKataSandi.route) {
            PemulihanKataSandiScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                })
        }

        composable(Screen.Home.route) {
            HomeScreen(
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
                    navController.navigate(Screen.Pelanggan.route) {
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
                })
        }

        composable(Screen.BuatPesanan.route) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Buat Pesanan")
            }
        }

        composable(Screen.Gudang.route) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Gudang")
            }
        }

        composable(Screen.Pelanggan.route) {
            PelangganScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetailCust = { id ->
                    navController.navigate(Screen.DetailPelanggan.createRoute(id)){
                        launchSingleTop = true
                    }
                },
                onNavigateToAddCust = {
                    navController.navigate(Screen.TambahPelanggan.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.TambahPelanggan.route) {
            TambahPelangganScreen(
                onNavigateToPelangganScreen = {
                    navController.navigate(Screen.Pelanggan.route) {
                        popUpTo(Screen.Pelanggan.route) { inclusive = true }
                    }
                },
                openMaps = { url ->
                    openMaps(context, url)
                })
        }

        composable(
            route = Screen.DetailPelanggan.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("id") ?: ""
            DetailPelangganScreen(
                idCust = id,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEditCust = { id ->
                    navController.navigate(Screen.EditPelanggan.createRoute(id)){
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

        composable(Screen.Riwayat.route) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Riwayat")
            }
        }

        composable(Screen.Performa.route) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Performa")
            }
        }

        composable(Screen.Pengaturan.route) {
            PengaturanScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
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
                }
            )
        }

        composable(Screen.GantiKataSandi.route) {
            GantiKataSandiScreen(onNavigateToSetting = { navController.popBackStack() })
        }

        composable(Screen.PemulihanAkun.route) {
            PemulihanAkunScreen(onNavigateToSetting = { navController.popBackStack() })
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun JJSembakoAppPreview() {
    JJSembakoTheme {
        JJSembakoApp()
    }
}