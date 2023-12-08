package com.dr.jjsembako.feature_home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.model.MenuInfo
import com.dr.jjsembako.navigation.Screen
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun MenuSection(
    onNavigateToCreateOrder: () -> Unit,
    onNavigateToWarehouse: () -> Unit,
    onNavigateToCustomer: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToPerformance: () -> Unit,
    onNavigateToSetting: () -> Unit,
    modifier: Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                menuList.take(3).forEachIndexed { index, menuInfo ->
                    MenuItem(
                        menuInfo = menuInfo,
                        onNavigateToCreateOrder = { onNavigateToCreateOrder() },
                        onNavigateToWarehouse = { onNavigateToWarehouse() },
                        onNavigateToCustomer = { onNavigateToCustomer() },
                        onNavigateToHistory = { onNavigateToHistory() },
                        onNavigateToPerformance = { onNavigateToPerformance() },
                        onNavigateToSetting = { onNavigateToSetting() },
                        modifier = modifier
                    )
                    if (index < 2) {
                        Spacer(modifier = modifier.width(8.dp))
                    }
                }
            }
            Spacer(modifier = modifier.height(8.dp))
            Row {
                menuList.takeLast(3).forEachIndexed { index, menuInfo ->
                    MenuItem(
                        menuInfo = menuInfo,
                        onNavigateToCreateOrder = { onNavigateToCreateOrder() },
                        onNavigateToWarehouse = { onNavigateToWarehouse() },
                        onNavigateToCustomer = { onNavigateToCustomer() },
                        onNavigateToHistory = { onNavigateToHistory() },
                        onNavigateToPerformance = { onNavigateToPerformance() },
                        onNavigateToSetting = { onNavigateToSetting() },
                        modifier = modifier
                    )
                    if (index < 2) {
                        Spacer(modifier = modifier.width(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuItem(
    menuInfo: MenuInfo,
    onNavigateToCreateOrder: () -> Unit,
    onNavigateToWarehouse: () -> Unit,
    onNavigateToCustomer: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToPerformance: () -> Unit,
    onNavigateToSetting: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                when (menuInfo.route) {
                    Screen.BuatPesanan.route -> onNavigateToCreateOrder()
                    Screen.Gudang.route -> onNavigateToWarehouse()
                    Screen.Pelanggan.route -> onNavigateToCustomer()
                    Screen.Riwayat.route -> onNavigateToHistory()
                    Screen.Performa.route -> onNavigateToPerformance()
                    Screen.Pengaturan.route -> onNavigateToSetting()
                    else -> {}
                }
            }
            .background(MaterialTheme.colorScheme.primary)
            .width(96.dp)
            .padding(horizontal = 4.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            menuInfo.icon,
            contentDescription = stringResource(R.string.icon_of_menu, menuInfo.title),
            tint = Color.White,
            modifier = modifier
                .size(32.dp)
                .padding(bottom = 8.dp)
        )
        Text(
            text = menuInfo.title,
            color = Color.White,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}

private val menuList = listOf(
    MenuInfo("Buat Pesanan", Icons.Default.AddShoppingCart, Screen.BuatPesanan.route),
    MenuInfo("Gudang", Icons.Default.Warehouse, Screen.Gudang.route),
    MenuInfo("Pelanggan", Icons.Default.People, Screen.Pelanggan.route),
    MenuInfo("Riwayat", Icons.Default.History, Screen.Riwayat.route),
    MenuInfo("Performa", Icons.Default.BarChart, Screen.Performa.route),
    MenuInfo("Pengaturan", Icons.Default.Settings, Screen.Pengaturan.route)
)

@Composable
@Preview(showBackground = true)
fun MenuItemPreview() {
    JJSembakoTheme {
        MenuItem(
            menuList[0],
            onNavigateToCreateOrder = {},
            onNavigateToWarehouse = {},
            onNavigateToCustomer = {},
            onNavigateToHistory = {},
            onNavigateToPerformance = {},
            onNavigateToSetting = {},
            modifier = Modifier
        )
    }
}

@Composable
@Preview(showBackground = true)
fun MenuSectionPreview() {
    JJSembakoTheme {
        MenuSection(
            onNavigateToCreateOrder = {},
            onNavigateToWarehouse = {},
            onNavigateToCustomer = {},
            onNavigateToHistory = {},
            onNavigateToPerformance = {},
            onNavigateToSetting = {},
            modifier = Modifier
        )
    }
}