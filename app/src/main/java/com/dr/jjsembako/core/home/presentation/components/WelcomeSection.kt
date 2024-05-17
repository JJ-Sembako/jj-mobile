package com.dr.jjsembako.core.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.BuildConfig
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.components.utils.DevMode
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun WelcomeSection(name: String, onLogout: () -> Unit, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Greetings(name, modifier)
        Icon(
            Icons.Default.ExitToApp,
            contentDescription = stringResource(R.string.logout),
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = modifier.clickable { onLogout() }
        )
    }
}

@Composable
private fun Greetings(name: String, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo_white),
            contentDescription = stringResource(R.string.app_logo),
            modifier = modifier
                .size(64.dp)
                .padding(end = 8.dp)
        )
        Column {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.padding(bottom = 4.dp)
                )
                if(BuildConfig.BUILD_TYPE == "debug"){
                    Spacer(modifier = modifier.width(8.dp))
                    DevMode(modifier)
                }
            }
            Text(text = "Hi, $name", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun WelcomeSectionPreview() {
    JJSembakoTheme {
        WelcomeSection(name = "UsernameIsCool12345678", onLogout = {}, modifier = Modifier)
    }
}

@Composable
@Preview(showBackground = true)
private fun GreetingsPreview() {
    JJSembakoTheme {
        Greetings(name = "UsernameIsCool12345678", modifier = Modifier)
    }
}