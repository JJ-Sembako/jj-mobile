package com.dr.jjsembako.feature_history.presentation.retur.create

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.dr.jjsembako.core.data.remote.response.order.DetailOrder
import com.dr.jjsembako.core.presentation.components.dialog.AlertErrorDialog
import com.dr.jjsembako.core.presentation.components.dialog.LoadingDialog
import com.dr.jjsembako.core.presentation.components.dialog.PreviewImageDialog
import com.dr.jjsembako.core.presentation.components.screen.ErrorScreen
import com.dr.jjsembako.core.presentation.components.screen.LoadingScreen
import com.dr.jjsembako.core.presentation.components.utils.HeaderError
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.DataMapper.mapDetailOrderDataToDataOrderHistoryCard
import com.dr.jjsembako.feature_history.presentation.components.ChangeTotalPayment
import com.dr.jjsembako.feature_history.presentation.components.PNRHeader
import com.dr.jjsembako.feature_history.presentation.components.retur.RSelectedProduct
import com.dr.jjsembako.feature_history.presentation.components.retur.RSelectedSubstitute
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@Composable
fun ReturScreen(
    id: String,
    context: Context,
    clipboardManager: ClipboardManager,
    onNavigateBack: () -> Unit,
    onSelectProduct: () -> Unit,
    onSelectSubstitute: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tag = "Retur-S"
    val viewModel: ReturViewModel = hiltViewModel()
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
                ReturContent(
                    orderData = orderData,
                    viewModel = viewModel,
                    context = context,
                    clipboardManager = clipboardManager,
                    onNavigateBack = { onNavigateBack() },
                    onSelectProduct = { onSelectProduct() },
                    onSelectSubstitute = { onSelectSubstitute() },
                    modifier = modifier
                )
            }
        }

        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun ReturContent(
    orderData: DetailOrder,
    viewModel: ReturViewModel,
    context: Context,
    clipboardManager: ClipboardManager,
    onNavigateBack: () -> Unit,
    onSelectProduct: () -> Unit,
    onSelectSubstitute: () -> Unit,
    modifier: Modifier
) {
    val tag = "Retur-C"
    val stateSecond = viewModel.stateSecond.observeAsState().value
    val stateRefresh = viewModel.stateRefresh.observeAsState().value
    val errorState = viewModel.errorState.observeAsState().value
    val errorMsg = viewModel.errorMsg.observeAsState().value
    val isRefreshing by viewModel.isRefreshing.collectAsState(initial = false)
    val statusCode = viewModel.statusCode.observeAsState().value
    val message = viewModel.message.observeAsState().value
    val selectedDataR = viewModel.selectedDataR.observeAsState().value
    val selectedDataS = viewModel.selectedDataS.observeAsState().value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val showErrMinus = rememberSaveable { mutableStateOf(false) }
    val showErrCantRetur = rememberSaveable { mutableStateOf(false) }
    val showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    val showErrorDialog = rememberSaveable { mutableStateOf(false) }
    val showPreviewImageDialog = remember { mutableStateOf(false) }
    val previewProductName = remember { mutableStateOf("") }
    val previewProductImage = remember { mutableStateOf("") }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refresh() })

    val changeCost = rememberSaveable { mutableLongStateOf(0L) }

    LaunchedEffect(Unit, selectedDataR, selectedDataS) {
        if (selectedDataR != null && selectedDataS != null) {
            changeCost.longValue =
                (selectedDataS.selledPrice - selectedDataR.selledPrice) * selectedDataR.amountSelected
            showErrMinus.value = changeCost.longValue < 0
        } else {
            changeCost.longValue = 0L
            showErrMinus.value = false
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
                title = { Text(stringResource(R.string.retur)) },
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
                        if (!showErrMinus.value && selectedDataR != null && selectedDataS != null) {
                            viewModel.handleCreateRetur()
                        } else showErrCantRetur.value = true
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
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
                .pullRefresh(pullRefreshState)
                .verticalScroll(scrollState),
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                if (showErrMinus.value) {
                    HeaderError(modifier = modifier, message = stringResource(R.string.err_minus_retur))
                    Spacer(modifier = modifier.height(4.dp))
                }

                if (errorState == true && !errorMsg.isNullOrEmpty()) {
                    HeaderError(modifier = modifier, message = errorMsg)
                    Spacer(modifier = modifier.height(16.dp))
                }

                PNRHeader(
                    data = mapDetailOrderDataToDataOrderHistoryCard(orderData),
                    context = context,
                    clipboardManager = clipboardManager,
                    modifier = modifier
                )
                Spacer(modifier = modifier.height(16.dp))
                RSelectedProduct(
                    data = selectedDataR,
                    viewModel = viewModel,
                    showDialog = showPreviewImageDialog,
                    previewProductName = previewProductName,
                    previewProductImage = previewProductImage,
                    onSelectProduct = { onSelectProduct() },
                    modifier = modifier
                )
                Spacer(modifier = modifier.height(16.dp))
                RSelectedSubstitute(
                    data = selectedDataS,
                    viewModel = viewModel,
                    showDialog = showPreviewImageDialog,
                    previewProductName = previewProductName,
                    previewProductImage = previewProductImage,
                    onSelectSubstitute = { onSelectSubstitute() },
                    modifier = modifier
                )
                Spacer(modifier = modifier.height(16.dp))
                ChangeTotalPayment(
                    orderCost = orderData.actualTotalPrice,
                    changeCost = changeCost.longValue,
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

                if (showErrCantRetur.value) {
                    AlertErrorDialog(
                        message = stringResource(R.string.err_fill_data_first),
                        showDialog = showErrCantRetur,
                        modifier = modifier
                    )
                }

                if (showPreviewImageDialog.value) {
                    PreviewImageDialog(
                        name = previewProductName.value,
                        image = previewProductImage.value,
                        showDialog = showPreviewImageDialog,
                        modifier = modifier
                    )
                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = modifier.align(Alignment.TopCenter)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun ReturScreenPreview() {
    JJSembakoTheme {
        ReturScreen(
            id = "123",
            context = LocalContext.current,
            clipboardManager = LocalClipboardManager.current,
            onNavigateBack = {},
            onSelectProduct = {},
            onSelectSubstitute = {}
        )
    }
}