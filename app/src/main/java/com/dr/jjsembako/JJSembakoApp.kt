package com.dr.jjsembako

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dr.jjsembako.navigation.Screen
import com.dr.jjsembako.ui.feature_auth.login.LoginScreen
import com.dr.jjsembako.ui.feature_home.HomeScreen
import com.dr.jjsembako.ui.feature_setting.change_password.GantiKataSandiScreen
import com.dr.jjsembako.ui.feature_setting.setting.PengaturanScreen
import com.dr.jjsembako.ui.theme.JJSembakoTheme

@Composable
fun JJSembakoApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController, onLoginSuccess = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            })
        }


        composable(Screen.Home.route) {
            HomeScreen(navController = navController, onLogout = {
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Pelanggan")
            }
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
            PengaturanScreen(navController = navController)
        }

        composable(Screen.GantiKataSandi.route) {
            GantiKataSandiScreen(onNavigateToSetting = {navController.popBackStack()})
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