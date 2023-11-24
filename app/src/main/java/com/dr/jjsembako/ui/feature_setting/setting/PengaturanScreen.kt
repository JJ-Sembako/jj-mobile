package com.dr.jjsembako.ui.feature_setting.setting

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dr.jjsembako.R
import com.dr.jjsembako.navigation.Screen
import com.dr.jjsembako.ui.theme.JJSembakoTheme

@Composable
fun PengaturanScreen(navController: NavController) {
    var username by rememberSaveable { mutableStateOf("username") }
    val context = LocalContext.current
    val versionName = getAppVersion(context)
    val isDarkTheme = isSystemInDarkTheme()
    val logo = if (isDarkTheme) {
        painterResource(id = R.drawable.app_logo_white)
    } else {
        painterResource(id = R.drawable.app_logo_black)
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // Logo and greetings
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = logo,
                contentDescription = stringResource(R.string.app_logo),
                modifier = Modifier
                    .size(150.dp)
                    .padding(top = 32.dp, bottom = 8.dp)
            )
            Text(text = "Hi, $username", fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                shape = RoundedCornerShape(12.dp),
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 8.dp)
            ) {
                Text(stringResource(R.string.ganti_kata_sandi))
            }
            Button(
                shape = RoundedCornerShape(12.dp),
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 8.dp)
            ) {
                Text(stringResource(R.string.pemulihan_akun))
            }
            Button(
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(stringResource(R.string.keluar))
            }
        }

        // Copyrights and version
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp, top = 128.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.milik),
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.perusahaan),
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
            Text(text = stringResource(R.string.versi, versionName), fontSize = 11.sp)
        }
    }
}

fun getAppVersion(context: Context): String {
    try {
        val packageManager = context.packageManager
        val packageName = context.packageName
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            packageManager.getPackageInfo(packageName, 0)
        }
        return packageInfo.versionName
    } catch (e: Exception) {
        return "x.x.x"
    }
}

@Composable
@Preview(showBackground = true)
fun PengaturanScreenPreview() {
    JJSembakoTheme {
        PengaturanScreen(navController = rememberNavController())
    }
}