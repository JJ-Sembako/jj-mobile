package com.dr.jjsembako

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.dr.jjsembako.feature_customer.presentation.list.PelangganScreen
import com.dr.jjsembako.feature_home.presentation.HomeScreen
import com.dr.jjsembako.feature_order.presentation.create_order.BuatPesananScreen
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                BuatPesananScreen()
            }
        }

        composable(Screen.Gudang.route) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GudangScreen(onNavigateBack = { navController.popBackStack() })
            }
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