package com.dr.jjsembako.feature_setting.presentation.setting

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun PengaturanScreen(
    onLogout: () -> Unit,
    onNavigateToChangePassword: () -> Unit,
    onNavigateToAccountRecovery: () -> Unit,
    getAppVersion: () -> String,
    modifier: Modifier = Modifier,
    username: String = "username"
) {
    val versionName = getAppVersion()
    val logo = if (isSystemInDarkTheme()) {
        painterResource(id = R.drawable.app_logo_white)
    } else {
        painterResource(id = R.drawable.app_logo_black)
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // Logo and greetings
        Column(
            modifier = modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = logo,
                contentDescription = stringResource(R.string.app_logo),
                modifier = modifier
                    .size(150.dp)
                    .padding(top = 32.dp, bottom = 8.dp)
            )
            Text(text = "Hi, $username", fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Spacer(modifier = modifier.height(8.dp))
        }

        // Button
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                shape = RoundedCornerShape(12.dp),
                onClick = { onNavigateToChangePassword() },
                modifier = modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(stringResource(R.string.change_password))
            }
            Spacer(modifier = modifier.height(8.dp))
            Button(
                shape = RoundedCornerShape(12.dp),
                onClick = { onNavigateToAccountRecovery() },
                modifier = modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(stringResource(R.string.account_recovery))
            }
            Spacer(modifier = modifier.height(8.dp))
            Button(
                shape = RoundedCornerShape(12.dp),
                onClick = { onLogout() },
                modifier = modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(stringResource(R.string.logout))
            }
        }

        // Copyrights and version
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp, top = 128.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.copyright),
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.company),
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
            Text(text = stringResource(R.string.version, versionName), fontSize = 11.sp)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PengaturanScreenPreview() {
    JJSembakoTheme {
        PengaturanScreen(
            onLogout = {},
            onNavigateToChangePassword = {},
            onNavigateToAccountRecovery = {},
            getAppVersion = { "x.x.x" }
        )
    }
}