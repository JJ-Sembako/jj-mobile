package com.dr.jjsembako.ui.feature_home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dr.jjsembako.ui.feature_home.components.HistorySection
import com.dr.jjsembako.ui.feature_home.components.MenuSection
import com.dr.jjsembako.ui.feature_home.components.OmzetSection
import com.dr.jjsembako.ui.feature_home.components.StatisticSection
import com.dr.jjsembako.ui.feature_home.components.WelcomeSection
import com.dr.jjsembako.ui.theme.JJSembakoTheme

@Composable
fun HomeScreen(navController: NavController, onLogout: () -> Unit, modifier: Modifier = Modifier) {
    var username by rememberSaveable { mutableStateOf("username") }
    var omzet by rememberSaveable { mutableStateOf(0L) }
    var totalPesanan by rememberSaveable { mutableStateOf(0) }
    var totalBarang by rememberSaveable { mutableStateOf(0) }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(224.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                WelcomeSection(name = username, onLogout = { onLogout() })
                Spacer(modifier = Modifier.height(8.dp))
                OmzetSection(omzet)
                Spacer(modifier = Modifier.height(8.dp))
                StatisticSection(totalPesanan, totalBarang)
            }
        }
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            MenuSection(navController)
            Spacer(modifier = Modifier.height(24.dp))
            HistorySection()
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