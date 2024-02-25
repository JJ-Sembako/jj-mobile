package com.dr.jjsembako.feature_history.presentation.potong_nota.create

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dr.jjsembako.R
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.remote.response.order.DetailOrderData
import com.dr.jjsembako.core.presentation.components.dialog.AlertErrorDialog
import com.dr.jjsembako.core.presentation.components.dialog.LoadingDialog
import com.dr.jjsembako.core.presentation.components.screen.ErrorScreen
import com.dr.jjsembako.core.presentation.components.screen.LoadingScreen
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.DataMapper.mapDetailOrderDataToDataOrderHistoryCard
import com.dr.jjsembako.feature_history.presentation.components.ChangeTotalPayment
import com.dr.jjsembako.feature_history.presentation.components.PNRHeader
import com.dr.jjsembako.feature_history.presentation.components.potong_nota.PNSelectedProduct
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlin.math.roundToInt

@Composable
fun PotongNotaScreen(
    id: String,
    context: Context,
    clipboardManager: ClipboardManager,
    onNavigateBack: () -> Unit,
    onSelectProduct: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tag = "PN-S"
    val viewModel: PotongNotaViewModel = hiltViewModel()
    val stateFirst = viewModel.stateFirst.observeAsState().value
    val statusCode = viewModel.statusCode.observeAsState().value
    val message = viewModel.message.observeAsState().value
    val orderData = viewModel.orderData

    // Set id for the first time Composable is rendered
    LaunchedEffect(id, Unit) {
        viewModel.setId(id)
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
                onReload = { viewModel.fetchOrder(id) },
                message = message ?: "Unknown error",
                modifier = modifier
            )
        }

        StateResponse.SUCCESS -> {
            if (orderData == null) {
                ErrorScreen(
                    onNavigateBack = { onNavigateBack() },
                    onReload = { viewModel.fetchOrder(id) },
                    message = "Server Error",
                    modifier = modifier
                )
            } else {
                PotongNotaContent(
                    orderData = orderData,
                    viewModel = viewModel,
                    context = context,
                    clipboardManager = clipboardManager,
                    onNavigateBack = { onNavigateBack() },
                    onSelectProduct = { onSelectProduct() },
                    modifier = modifier
                )
            }
        }

        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun PotongNotaContent(
    orderData: DetailOrderData,
    viewModel: PotongNotaViewModel,
    context: Context,
    clipboardManager: ClipboardManager,
    onNavigateBack: () -> Unit,
    onSelectProduct: () -> Unit,
    modifier: Modifier
) {
    val tag = "PBPN-C"
    val stateSecond = viewModel.stateSecond.observeAsState().value
    val stateRefresh = viewModel.stateRefresh.observeAsState().value
    val isRefreshing by viewModel.isRefreshing.collectAsState(initial = false)
    val statusCode = viewModel.statusCode.observeAsState().value
    val message = viewModel.message.observeAsState().value
    val selectedData = viewModel.selectedData.observeAsState().value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val showErrCantCanceled = rememberSaveable { mutableStateOf(false) }
    val showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    val showErrorDialog = rememberSaveable { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refresh() })

    val changeCost = rememberSaveable { mutableLongStateOf(0L) }
    val changeQty = rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(Unit, selectedData) {
        if (selectedData != null) {
            changeQty.intValue = -1 * selectedData.amountSelected
            changeCost.longValue = -1 * selectedData.amountSelected * selectedData.selledPrice
        } else {
            changeCost.longValue = 0L
            changeQty.intValue = 0
        }
    }

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
            viewModel.setStateSecond(null)
        }

        StateResponse.SUCCESS -> {
            showLoadingDialog.value = false
            showErrorDialog.value = false
            viewModel.reset()
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
            viewModel.setStateRefresh(null)
        }

        StateResponse.SUCCESS -> {
            showLoadingDialog.value = false
            showErrorDialog.value = false
            viewModel.setStateRefresh(null)
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
                title = { Text(stringResource(R.string.potong_nota)) },
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
                    IconButton(onClick = {
                        if (selectedData != null) viewModel.handleCreateCanceled()
                        else showErrCantCanceled.value = true
                    }) {
                        Icon(
                            Icons.Default.Check,
                            stringResource(R.string.finish),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    })
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

            PNRHeader(
                data = mapDetailOrderDataToDataOrderHistoryCard(orderData),
                context = context,
                clipboardManager = clipboardManager,
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))
            PNSelectedProduct(
                data = selectedData,
                viewModel = viewModel,
                onSelectProduct = { onSelectProduct() },
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))
            ChangeTotalPayment(
                orderCost = orderData.actualTotalPrice,
                changeCost = changeCost.longValue,
                modifier = modifier,
                isForUpdate = true,
                changeQty = changeQty.intValue
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

            if (showErrCantCanceled.value) {
                AlertErrorDialog(
                    message = stringResource(R.string.err_fill_data_first),
                    showDialog = showErrorDialog,
                    modifier = modifier
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PotongNotaScreenPreview() {
    JJSembakoTheme {
        PotongNotaScreen(
            id = "123",
            context = LocalContext.current,
            clipboardManager = LocalClipboardManager.current,
            onNavigateBack = {},
            onSelectProduct = {}
        )
    }
}