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
import androidx.compose.runtime.mutableIntStateOf
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
import com.dr.jjsembako.core.presentation.components.dialog.AlertDeleteDialog
import com.dr.jjsembako.core.presentation.components.dialog.AlertErrorDialog
import com.dr.jjsembako.core.presentation.components.dialog.LoadingDialog
import com.dr.jjsembako.core.presentation.components.dialog.OrderInformationDialog
import com.dr.jjsembako.core.presentation.components.dialog.SuccessDialog
import com.dr.jjsembako.core.presentation.components.screen.ErrorScreen
import com.dr.jjsembako.core.presentation.components.screen.LoadingScreen
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.DataMapper.mapDetailOrderDataToDataOrderHistoryCard
import com.dr.jjsembako.core.utils.DataMapper.mapDetailOrderDataToDataOrderTimestamps
import com.dr.jjsembako.feature_history.domain.model.UpdateStateOrder
import com.dr.jjsembako.feature_history.presentation.components.detail.CustomerInformation
import com.dr.jjsembako.feature_history.presentation.components.detail.OrderButtonMenu
import com.dr.jjsembako.feature_history.presentation.components.detail.OrderInformation
import com.dr.jjsembako.feature_history.presentation.components.detail.OrderTimestamps
import com.dr.jjsembako.feature_history.presentation.components.detail.OrderedProductList
import com.dr.jjsembako.feature_history.presentation.components.detail.ReturPotongNotaInformation
import com.dr.jjsembako.feature_history.presentation.components.dialog.CancelPotongNotaDialog
import com.dr.jjsembako.feature_history.presentation.components.dialog.CancelReturDialog
import com.dr.jjsembako.feature_history.presentation.components.dialog.DeleteProductDialog
import com.dr.jjsembako.feature_history.presentation.components.dialog.PaymentDialog
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
    val stateSecond = detailTransaksiViewModel.stateSecond.observeAsState().value
    val stateUpdate = detailTransaksiViewModel.stateUpdate.observeAsState().value
    val stateRefresh = detailTransaksiViewModel.stateRefresh.observeAsState().value
    val isRefreshing by detailTransaksiViewModel.isRefreshing.collectAsState(initial = false)
    val message = detailTransaksiViewModel.message.observeAsState().value
    val statusCode = detailTransaksiViewModel.statusCode.observeAsState().value
    val orderInfo = mapDetailOrderDataToDataOrderHistoryCard(orderData)

    val scrollState = rememberScrollState()
    val showInfoDialog = remember { mutableStateOf(false) }
    val showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    val showErrorDialog = remember { mutableStateOf(false) }
    val showSuccessDialog = remember { mutableStateOf(false) }
    val showPaymentDialog = remember { mutableStateOf(false) }
    val showDeleteOrder = remember { mutableStateOf(false) }
    val showDeleteProductOrderDialog = remember { mutableStateOf(false) }
    val showDeleteCanceledDialog = remember { mutableStateOf(false) }
    val showDeleteReturDialog = remember { mutableStateOf(false) }
    val showCantModifyOrderDialog = remember { mutableStateOf(false) }
    val showCantDelOrderDialog = remember { mutableStateOf(false) }
    val showCantPNRDialog = remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    val idDeleteProductOrder = remember { mutableStateOf("") }
    val idDeleteCanceled = remember { mutableStateOf("") }
    val idDeleteRetur = remember { mutableStateOf("") }
    val statusCanceled = remember { mutableIntStateOf(0) }
    val statusRetur = remember { mutableIntStateOf(0) }
    val msgErrorPNR = rememberSaveable { mutableStateOf("") }
    val msgSuccess = rememberSaveable { mutableStateOf("") }
    val msgSuccessOption = listOf(
        stringResource(R.string.success_payment),
        stringResource(R.string.success_delete_product),
        stringResource(R.string.success_delete_pn),
        stringResource(R.string.success_delete_retur),
    )

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { detailTransaksiViewModel.refresh() })

    when (stateSecond) {
        StateResponse.LOADING -> {
            showLoadingDialog.value = true
        }

        StateResponse.ERROR -> {
            Log.e(tag, "Error")
            Log.e(tag, "state: $stateSecond")
            Log.e(tag, "Error: $message")
            Log.e(tag, "statusCode: $statusCode")
            showLoadingDialog.value = false
            showErrorDialog.value = true
            detailTransaksiViewModel.setStateSecond(null)
        }

        StateResponse.SUCCESS -> {
            showLoadingDialog.value = false
            showErrorDialog.value = false
            detailTransaksiViewModel.setStateSecond(null)
        }

        else -> {}
    }

    when (stateUpdate) {
        UpdateStateOrder.PAYMENT -> {
            showSuccessDialog.value = true
            msgSuccess.value = msgSuccessOption[0]
            detailTransaksiViewModel.setStateUpdate(null)
        }

        UpdateStateOrder.DEL_PRODUCT -> {
            showSuccessDialog.value = true
            msgSuccess.value = msgSuccessOption[1]
            detailTransaksiViewModel.setStateUpdate(null)
        }

        UpdateStateOrder.DEL_CANCELED -> {
            showSuccessDialog.value = true
            msgSuccess.value = msgSuccessOption[2]
            detailTransaksiViewModel.setStateUpdate(null)
        }

        UpdateStateOrder.DEL_RETUR -> {
            showSuccessDialog.value = true
            msgSuccess.value = msgSuccessOption[3]
            detailTransaksiViewModel.setStateUpdate(null)
        }

        UpdateStateOrder.DEL_ORDER -> {
            detailTransaksiViewModel.setStateUpdate(null)
            onNavigateBack()
        }

        else -> {}
    }

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
                                if (orderData.orderStatus != 0 && orderData.orderStatus != 1){
                                    showCantModifyOrderDialog.value = true
                                } else onNavigateToAddProductOrder()
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.edit_product_order)) },
                            onClick = {
                                menuExpanded = false
                                if (orderData.orderStatus != 0 && orderData.orderStatus != 1){
                                    showCantModifyOrderDialog.value = true
                                } else onNavigateToEditProductOrder()
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.cancel_order)) },
                            onClick = {
                                menuExpanded = false
                                if (orderData.orderStatus != 0 && orderData.orderStatus != 1) {
                                    showCantDelOrderDialog.value = true
                                } else showDeleteOrder.value = true
                            })
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
                data = orderInfo,
                showCantPNRDialog = showCantPNRDialog,
                showPaymentDialog = showPaymentDialog,
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
                data = orderInfo,
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))
            OrderedProductList(
                data = orderData.orderToProducts,
                totalPrice = orderData.totalPrice,
                showDialog = showDeleteProductOrderDialog,
                idDeleteProductOrder = idDeleteProductOrder,
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))
            OrderTimestamps(
                data = mapDetailOrderDataToDataOrderTimestamps(orderData),
                modifier = modifier
            )
            Spacer(modifier = modifier.height(64.dp))
            ReturPotongNotaInformation(
                dataCanceled = orderData.canceled,
                dataRetur = orderData.retur,
                actualTotalPrice = orderData.actualTotalPrice,
                showDialogCanceled = showDeleteCanceledDialog,
                showDialogRetur = showDeleteReturDialog,
                idDeleteCanceled = idDeleteCanceled,
                idDeleteRetur = idDeleteRetur,
                statusCanceled = statusCanceled,
                statusRetur = statusRetur,
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))

            if (showLoadingDialog.value) {
                LoadingDialog(showLoadingDialog, modifier)
            }

            if (showErrorDialog.value) {
                AlertErrorDialog(
                    message = message ?: "Unknown error",
                    showDialog = showErrorDialog,
                    modifier = modifier
                )
            }

            if (showInfoDialog.value) {
                OrderInformationDialog(showDialog = showInfoDialog, modifier = modifier)
            }

            if (showCantPNRDialog.value) {
                AlertErrorDialog(
                    header = stringResource(R.string.action_denied),
                    message = msgErrorPNR.value,
                    showDialog = showCantPNRDialog,
                    modifier = modifier
                )
            }

            if (showCantModifyOrderDialog.value) {
                AlertErrorDialog(
                    header = stringResource(R.string.action_denied),
                    message = stringResource(R.string.err_edit_order),
                    showDialog = showCantModifyOrderDialog,
                    modifier = modifier
                )
            }

            if (showCantDelOrderDialog.value) {
                AlertErrorDialog(
                    header = stringResource(R.string.action_denied),
                    message = stringResource(R.string.err_del_order),
                    showDialog = showCantDelOrderDialog,
                    modifier = modifier
                )
            }

            if (showPaymentDialog.value) {
                PaymentDialog(
                    paymentStatus = orderData.paymentStatus,
                    showDialog = showPaymentDialog,
                    handleUpdatePaymentStatus = { detailTransaksiViewModel.handleUpdatePaymentStatus() },
                    modifier = modifier
                )
            }

            if (showDeleteProductOrderDialog.value) {
                DeleteProductDialog(
                    orderStatus = orderData.orderStatus,
                    showDialog = showDeleteProductOrderDialog,
                    handleDeleteProductOrder = {
                        detailTransaksiViewModel.handleDeleteProductOrder(
                            idDeleteProductOrder.value
                        )
                    },
                    modifier = modifier
                )
            }

            if (showDeleteCanceledDialog.value) {
                CancelPotongNotaDialog(
                    status = statusCanceled.intValue,
                    showDialog = showDeleteCanceledDialog,
                    handleDeleteCanceled = {
                        detailTransaksiViewModel.handleDeleteCanceled(
                            idDeleteCanceled.value
                        )
                    },
                    modifier = modifier
                )
            }

            if (showDeleteReturDialog.value) {
                CancelReturDialog(
                    status = statusRetur.intValue,
                    showDialog = showDeleteReturDialog,
                    handleDeleteRetur = { detailTransaksiViewModel.handleDeleteRetur(idDeleteRetur.value) },
                    modifier = modifier
                )
            }

            if (showDeleteOrder.value) {
                AlertDeleteDialog(
                    onDelete = { detailTransaksiViewModel.handleDeleteOrder() },
                    showDialog = showDeleteOrder,
                    modifier = modifier
                )
            }

            if (showSuccessDialog.value) {
                SuccessDialog(
                    message = msgSuccess.value,
                    showDialog = showSuccessDialog,
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