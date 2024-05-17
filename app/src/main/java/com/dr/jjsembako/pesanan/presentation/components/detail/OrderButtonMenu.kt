package com.dr.jjsembako.pesanan.presentation.components.detail

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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.dummy.dataOrderDataItem
import com.dr.jjsembako.core.data.model.MenuOrderInfo
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.pesanan.domain.model.DataOrderHistoryCard

@Composable
fun OrderButtonMenu(
    data: DataOrderHistoryCard,
    showCantPNRDialog: MutableState<Boolean>,
    showPaymentDialog: MutableState<Boolean>,
    msgErrorPNR: MutableState<String>,
    openMaps: (String) -> Unit,
    call: (String) -> Unit,
    chatWA: (String) -> Unit,
    onNavigateToPotongNota: () -> Unit,
    onNavigateToRetur: () -> Unit,
    modifier: Modifier
) {
    val menuList = listOf(
        MenuOrderInfo(0, Icons.Default.LocationOn, stringResource(R.string.menu_open_maps)),
        MenuOrderInfo(1, Icons.Default.Call, stringResource(R.string.menu_call)),
        MenuOrderInfo(2, Icons.Default.Chat, stringResource(R.string.menu_chat_wa)),
        MenuOrderInfo(
            3,
            Icons.Default.MonetizationOn,
            stringResource(R.string.menu_confirm_payment)
        ),
        MenuOrderInfo(4, Icons.Default.Repeat, stringResource(R.string.menu_retur)),
        MenuOrderInfo(5, Icons.Default.ContentCut, stringResource(R.string.menu_potong_nota)),
    )

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                menuList.take(3).forEachIndexed { index, menuInfo ->
                    MenuItem(
                        data = data,
                        showCantPNRDialog = showCantPNRDialog,
                        showPaymentDialog = showPaymentDialog,
                        msgErrorPNR = msgErrorPNR,
                        menu = menuInfo,
                        openMaps = { url -> openMaps(url) },
                        call = { uri -> call(uri) },
                        chatWA = { url -> chatWA(url) },
                        onNavigateToPotongNota = { onNavigateToPotongNota() },
                        onNavigateToRetur = { onNavigateToRetur() },
                        modifier = modifier
                    )
                    if (index < 2) {
                        Spacer(modifier = modifier.width(8.dp))
                    }
                }
            }
            Spacer(modifier = modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                menuList.takeLast(3).forEachIndexed { index, menuInfo ->
                    MenuItem(
                        data = data,
                        showCantPNRDialog = showCantPNRDialog,
                        showPaymentDialog = showPaymentDialog,
                        msgErrorPNR = msgErrorPNR,
                        menu = menuInfo,
                        openMaps = { url -> openMaps(url) },
                        call = { uri -> call(uri) },
                        chatWA = { url -> chatWA(url) },
                        onNavigateToPotongNota = { onNavigateToPotongNota() },
                        onNavigateToRetur = { onNavigateToRetur() },
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
    data: DataOrderHistoryCard,
    showCantPNRDialog: MutableState<Boolean>,
    showPaymentDialog: MutableState<Boolean>,
    msgErrorPNR: MutableState<String>,
    menu: MenuOrderInfo,
    openMaps: (String) -> Unit,
    call: (String) -> Unit,
    chatWA: (String) -> Unit,
    onNavigateToPotongNota: () -> Unit,
    onNavigateToRetur: () -> Unit,
    modifier: Modifier
) {
    val msgErrorCantPNR = stringResource(R.string.info_cannot_pnr)
    val msgErrorCantPN = stringResource(R.string.info_cannot_pn)

    LaunchedEffect(Unit) {
        msgErrorPNR.value =
            if (data.orderStatus != 3) msgErrorCantPNR
            else if (data.paymentStatus == 1) msgErrorCantPN
            else ""
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                when (menu.id) {
                    0 -> { /* maps */
                        openMaps(data.customer.gmapsLink)
                    }

                    1 -> { /* call */
                        call(data.customer.phoneNumber)
                    }

                    2 -> { /* chat WA */
                        chatWA(data.customer.phoneNumber)
                    }

                    3 -> { /* payment confirmation */
                        showPaymentDialog.value = true
                    }

                    4 -> { /* Retur */
                        if (data.orderStatus != 3) {
                            showCantPNRDialog.value = true
                        } else onNavigateToRetur()
                    }

                    5 -> { /* Potong Nota */
                        if (data.orderStatus != 3 || data.paymentStatus == 1) {
                            showCantPNRDialog.value = true
                        } else onNavigateToPotongNota()
                    }

                    else -> {}
                }
            }
            .background(MaterialTheme.colorScheme.primary)
            .width(96.dp)
            .height(96.dp)
            .padding(horizontal = 4.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            menu.icon,
            contentDescription = stringResource(R.string.icon_of_menu, menu.title),
            tint = Color.White,
            modifier = modifier
                .size(32.dp)
                .padding(bottom = 8.dp)
        )
        Text(
            text = menu.title, color = Color.White, fontSize = 10.sp, textAlign = TextAlign.Center,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))

        )
    }
}

@Composable
@Preview(showBackground = true)
private fun OrderButtonMenuPreview() {
    JJSembakoTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OrderButtonMenu(
                data = dataOrderDataItem,
                showCantPNRDialog = remember { mutableStateOf(true) },
                showPaymentDialog = remember { mutableStateOf(true) },
                msgErrorPNR = remember { mutableStateOf("") },
                openMaps = {},
                call = {},
                chatWA = {},
                onNavigateToPotongNota = {},
                onNavigateToRetur = {},
                modifier = Modifier
            )
        }
    }
}