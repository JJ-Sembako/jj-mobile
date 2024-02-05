package com.dr.jjsembako.feature_history.presentation.detail

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dr.jjsembako.R
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.remote.response.order.DetailOrderData
import com.dr.jjsembako.core.presentation.components.dialog.AlertErrorDialog
import com.dr.jjsembako.core.presentation.components.dialog.LoadingDialog
import com.dr.jjsembako.core.presentation.components.dialog.OrderInformationDialog
import com.dr.jjsembako.core.presentation.components.screen.ErrorScreen
import com.dr.jjsembako.core.presentation.components.screen.LoadingScreen
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.DataMapper.mapDetailOrderDataToDataOrderHistoryCard
import com.dr.jjsembako.core.utils.DataMapper.mapDetailOrderDataToDataOrderTimestamps
import com.dr.jjsembako.feature_history.presentation.components.detail.CustomerInformation
import com.dr.jjsembako.feature_history.presentation.components.detail.OrderButtonMenu
import com.dr.jjsembako.feature_history.presentation.components.detail.OrderInformation
import com.dr.jjsembako.feature_history.presentation.components.detail.OrderTimestamps
import com.dr.jjsembako.feature_history.presentation.components.detail.OrderedProductList
import com.dr.jjsembako.feature_history.presentation.components.detail.ReturPotongNotaInformation
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlin.math.roundToInt

@Composable
fun DetailTransaksi(
    id: String,
    context: Context,
    clipboardManager: ClipboardManager,
    openMaps: (String) -> Unit,
    call: (String) -> Unit,
    chatWA: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToAddProductOrder: () -> Unit,
    onNavigateToEditProductOrder: () -> Unit,
    onNavigateToPotongNota: () -> Unit,
    onNavigateToRetur: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tag = "DetailTransaksi-S"
    val detailTransaksiViewModel: DetailTransaksiViewModel = hiltViewModel()
    val stateFirst = detailTransaksiViewModel.stateFirst.observeAsState().value
    val statusCode = detailTransaksiViewModel.statusCode.observeAsState().value
    val message = detailTransaksiViewModel.message.observeAsState().value
    val orderData = detailTransaksiViewModel.orderData

    // Set id for the first time Composable is rendered
    LaunchedEffect(id) {
        detailTransaksiViewModel.setId(id)
    }

    when (stateFirst) {
        StateResponse.LOADING -> {
            LoadingScreen(modifier = modifier)
        }

        StateResponse.ERROR -> {
            Log.e(tag, "Error")
            Log.e(tag, "state: $stateFirst")
            Log.e(tag, "Error: $message")
            Log.e(tag, "statusCode: $statusCode")
            ErrorScreen(
                onNavigateBack = { onNavigateBack() },
                onReload = { detailTransaksiViewModel.fetchOrder(id) },
                message = message ?: "Unknown error",
                modifier = modifier
            )
        }

        StateResponse.SUCCESS -> {
            if (orderData == null) {
                ErrorScreen(
                    onNavigateBack = { onNavigateBack() },
                    onReload = { detailTransaksiViewModel.fetchOrder(id) },
                    message = "Server Error",
                    modifier = modifier
                )
            } else {
                DetailTransaksiContent(
                    orderData = orderData,
                    context = context,
                    clipboardManager = clipboardManager,
                    openMaps = { url -> openMaps(url) },
                    call = { uri -> call(uri) },
                    chatWA = { url -> chatWA(url) },
                    onNavigateBack = { onNavigateBack() },
                    onNavigateToAddProductOrder = { onNavigateToAddProductOrder() },
                    onNavigateToEditProductOrder = { onNavigateToEditProductOrder() },
                    onNavigateToPotongNota = { onNavigateToPotongNota() },
                    onNavigateToRetur = { onNavigateToRetur() },
                    detailTransaksiViewModel = detailTransaksiViewModel,
                    modifier = modifier
                )
            }
        }

        else -> {}
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailTransaksiContent(
    orderData: DetailOrderData,
    context: Context,
    clipboardManager: ClipboardManager,
    openMaps: (String) -> Unit,
    call: (String) -> Unit,
    chatWA: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToAddProductOrder: () -> Unit,
    onNavigateToEditProductOrder: () -> Unit,
    onNavigateToPotongNota: () -> Unit,
    onNavigateToRetur: () -> Unit,
    detailTransaksiViewModel: DetailTransaksiViewModel,
    modifier: Modifier
) {
    val tag = "DetailTransaksi-C"
    val stateRefresh = detailTransaksiViewModel.stateRefresh.observeAsState().value
    val isRefreshing by detailTransaksiViewModel.isRefreshing.collectAsState(initial = false)
    val message = detailTransaksiViewModel.message.observeAsState().value
    val statusCode = detailTransaksiViewModel.statusCode.observeAsState().value

    val scrollState = rememberScrollState()
    val showInfoDialog = remember { mutableStateOf(false) }
    val showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    val showErrorDialog = remember { mutableStateOf(false) }
    val showCantPNRDialog = remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }

    val msgErrorPNR = rememberSaveable { mutableStateOf("") }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { detailTransaksiViewModel.refresh() })

    when (stateRefresh) {
        StateResponse.LOADING -> {
            showLoadingDialog.value = true
        }

        StateResponse.ERROR -> {
            Log.e(tag, "Error")
            Log.e(tag, "state: $stateRefresh")
            Log.e(tag, "Error: $message")
            Log.e(tag, "statusCode: $statusCode")
            showLoadingDialog.value = false
            showErrorDialog.value = true
            detailTransaksiViewModel.setStateRefresh(null)
        }

        StateResponse.SUCCESS -> {
            showLoadingDialog.value = false
            showErrorDialog.value = false
            detailTransaksiViewModel.setStateRefresh(null)
        }

        else -> {}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = { Text(stringResource(R.string.det_order)) },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showInfoDialog.value = !showInfoDialog.value }) {
                        Icon(
                            Icons.Default.Info,
                            stringResource(R.string.info),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(onClick = { menuExpanded = !menuExpanded }) {
                        Icon(
                            Icons.Default.MoreVert,
                            stringResource(R.string.menu),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    DropdownMenu(
                        modifier = modifier.widthIn(min = 200.dp),
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.add_product_order)) },
                            onClick = {
                                menuExpanded = false
                                onNavigateToAddProductOrder()
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.edit_product_order)) },
                            onClick = {
                                menuExpanded = false
                                onNavigateToEditProductOrder()
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.cancel_order)) },
                            onClick = { showDialog.value = true })
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .verticalScroll(scrollState)
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = modifier
                    .fillMaxWidth()
                    .height((pullRefreshState.progress * 100).roundToInt().dp)
            )

            OrderInformation(
                data = mapDetailOrderDataToDataOrderHistoryCard(orderData),
                context = context,
                clipboardManager = clipboardManager,
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))
            OrderButtonMenu(
                data = mapDetailOrderDataToDataOrderHistoryCard(orderData),
                showCantPNRDialog = showCantPNRDialog,
                msgErrorPNR = msgErrorPNR,
                openMaps = { url -> openMaps(url) },
                call = { uri -> call(uri) },
                chatWA = { url -> chatWA(url) },
                onNavigateToPotongNota = { onNavigateToPotongNota() },
                onNavigateToRetur = { onNavigateToRetur() },
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))
            CustomerInformation(
                data = mapDetailOrderDataToDataOrderHistoryCard(orderData),
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))
            OrderedProductList(modifier)
            Spacer(modifier = modifier.height(16.dp))
            OrderTimestamps(
                data = mapDetailOrderDataToDataOrderTimestamps(orderData),
                modifier = modifier
            )
            Spacer(modifier = modifier.height(64.dp))
            ReturPotongNotaInformation(modifier)
            Spacer(modifier = modifier.height(16.dp))

            if (showLoadingDialog.value) {
                LoadingDialog(showLoadingDialog, modifier)
            }

            if (showInfoDialog.value) {
                OrderInformationDialog(showDialog = showInfoDialog, modifier = modifier)
            }

            if (showCantPNRDialog.value) {
                AlertErrorDialog(
                    message = msgErrorPNR.value,
                    showDialog = showCantPNRDialog,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun DetailTransaksiPreview() {
    JJSembakoTheme {
        DetailTransaksi(
            id = "",
            context = LocalContext.current,
            clipboardManager = LocalClipboardManager.current,
            openMaps = {},
            call = {},
            chatWA = {},
            onNavigateBack = {},
            onNavigateToAddProductOrder = {},
            onNavigateToEditProductOrder = {},
            onNavigateToPotongNota = {},
            onNavigateToRetur = {},
            modifier = Modifier
        )
    }
}