package com.dr.jjsembako.ui.feature_home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.ui.theme.JJSembakoTheme

@Composable
fun WelcomeSection(name: String, onLogout: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Greetings(name)
        Icon(
            Icons.Default.ExitToApp,
            contentDescription = stringResource(R.string.keluar),
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.clickable { onLogout() }
        )
    }
}

@Composable
private fun Greetings(name: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo_white),
            contentDescription = stringResource(R.string.app_logo),
            modifier = Modifier
                .size(64.dp)
                .padding(end = 8.dp)
        )
        Column {
            Text(
                text = stringResource(R.string.app_name), fontSize = 20.sp, color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(text = "Hi, $name", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun WelcomeSectionPreview() {
    JJSembakoTheme {
        WelcomeSection(name = "UsernameIsCool12345678", onLogout = {})
    }
}

@Composable
@Preview(showBackground = true)
fun GreetingsPreview() {
    JJSembakoTheme {
        Greetings(name = "UsernameIsCool12345678")
    }
}