package com.dr.jjsembako

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dr.jjsembako.navigation.Screen
import com.dr.jjsembako.ui.feature_auth.LoginScreen
import com.dr.jjsembako.ui.feature_home.HomeScreen
import com.dr.jjsembako.ui.theme.JJSembakoTheme

@Composable
fun JJSembakoApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController, onLoginSuccess = {
                navController.navigate(Screen.Home.route){
                    popUpTo(Screen.Login.route){ inclusive=true}
                }
            })
        }


        composable(Screen.Home.route) {
            HomeScreen(navController = navController, onLogout = {
                navController.navigate(Screen.Login.route){
                    popUpTo(Screen.Home.route){ inclusive=true}
                }
            })
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