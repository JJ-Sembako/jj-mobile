package com.dr.jjsembako.feature_home.presentation

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
import com.dr.jjsembako.feature_home.presentation.components.HistorySection
import com.dr.jjsembako.feature_home.presentation.components.MenuSection
import com.dr.jjsembako.feature_home.presentation.components.OmzetSection
import com.dr.jjsembako.feature_home.presentation.components.StatisticSection
import com.dr.jjsembako.feature_home.presentation.components.WelcomeSection
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun HomeScreen(
    onNavigateToCreateOrder: () -> Unit,
    onNavigateToWarehouse: () -> Unit,
    onNavigateToCustomer: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToPerformance: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
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
        Box(modifier = modifier.fillMaxWidth()) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(224.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Column(
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = modifier.height(16.dp))
                WelcomeSection(name = username, onLogout = { onLogout() }, modifier = modifier)
                Spacer(modifier = modifier.height(8.dp))
                OmzetSection(omzet, modifier)
                Spacer(modifier = modifier.height(8.dp))
                StatisticSection(totalPesanan, totalBarang, modifier)
            }
        }
        Column {
            Spacer(modifier = modifier.height(16.dp))
            MenuSection(
                onNavigateToCreateOrder = { onNavigateToCreateOrder() },
                onNavigateToWarehouse = { onNavigateToWarehouse() },
                onNavigateToCustomer = { onNavigateToCustomer() },
                onNavigateToHistory = { onNavigateToHistory() },
                onNavigateToPerformance = { onNavigateToPerformance() },
                onNavigateToSetting = { onNavigateToSetting() },
                modifier = modifier
            )
            Spacer(modifier = modifier.height(24.dp))
            HistorySection(modifier = modifier)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    JJSembakoTheme {
        HomeScreen(
            onNavigateToCreateOrder = {},
            onNavigateToWarehouse = {},
            onNavigateToCustomer = {},
            onNavigateToHistory = {},
            onNavigateToPerformance = {},
            onNavigateToSetting = {},
            onLogout = {}
        )
    }
}