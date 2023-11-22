package com.dr.jjsembako.ui.feature_home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dr.jjsembako.R
import com.dr.jjsembako.ui.theme.JJSembakoTheme

@Composable
fun HomeScreen(navController: NavController, onLogout: ()->Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Home")
        Button(
            onClick = {
                onLogout()
            },
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(stringResource(R.string.keluar))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    JJSembakoTheme {
        HomeScreen(navController = rememberNavController(), onLogout = {})
    }
}