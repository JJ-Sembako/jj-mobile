package com.dr.jjsembako.feature_order.presentation.create_order

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asLiveData
import com.dr.jjsembako.R
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.presentation.components.dialog.AlertErrorDialog
import com.dr.jjsembako.core.presentation.components.dialog.LoadingDialog
import com.dr.jjsembako.core.presentation.components.screen.ErrorScreen
import com.dr.jjsembako.core.presentation.components.screen.LoadingScreen
import com.dr.jjsembako.core.presentation.components.utils.HeaderError
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_order.domain.model.ErrValidationCreateOrder
import com.dr.jjsembako.feature_order.presentation.components.SelectCustomer
import com.dr.jjsembako.feature_order.presentation.components.SelectPayment
import com.dr.jjsembako.feature_order.presentation.components.SelectProduct
import com.dr.jjsembako.feature_order.presentation.components.TotalPayment
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlin.math.roundToInt

@Composable
fun BuatPesananScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSelectCustomer: () -> Unit,
    onNavigateToSelectProduct: () -> Unit,
    onNavigateToDetailTransaction: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tag = "Buat Pesanan Screen"
    val buatPesananViewModel: BuatPesananViewModel = hiltViewModel()
    val state = buatPesananViewModel.state.observeAsState().value
    val message = buatPesananViewModel.message.observeAsState().value
    val statusCode = buatPesananViewModel.statusCode.observeAsState().value
    val idCust = buatPesananViewModel.idCustomer.collectAsState().value

    if (idCust.isEmpty()) {
        BuatPesananContent(
            buatPesananViewModel = buatPesananViewModel,
            onNavigateBack = { onNavigateBack() },
            onNavigateToSelectCustomer = { onNavigateToSelectCustomer() },
            onNavigateToSelectProduct = { onNavigateToSelectProduct() },
            onNavigateToDetailTransaction = { id -> onNavigateToDetailTransaction(id) }
        )
    } else {
        LaunchedEffect(idCust) {
            if (idCust.isNotEmpty()) {
                buatPesananViewModel.fetchDetailCustomer(idCust)
            }
        }
        when (state) {
            StateResponse.LOADING -> {
                LoadingScreen(modifier = modifier)
            }

            StateResponse.ERROR -> {
                Log.e(tag, "Error")
                Log.e(tag, "state: $state")
                Log.e(tag, "Error: $message")
                Log.e(tag, "statusCode: $statusCode")
                ErrorScreen(
                    onNavigateBack = { onNavigateBack() },
                    onReload = {
                        buatPesananViewModel.fetchDetailCustomer(idCust)
                    },
                    message = message ?: "Unknown error",
                    modifier = modifier
                )
            }

            StateResponse.SUCCESS -> {
                BuatPesananContent(
                    buatPesananViewModel = buatPesananViewModel,
                    onNavigateBack = { onNavigateBack() },
                    onNavigateToSelectCustomer = { onNavigateToSelectCustomer() },
                    onNavigateToSelectProduct = { onNavigateToSelectProduct() },
                    onNavigateToDetailTransaction = { id -> onNavigateToDetailTransaction(id) }
                )
            }

            else -> {}
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun BuatPesananContent(
    buatPesananViewModel: BuatPesananViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToSelectCustomer: () -> Unit,
    onNavigateToSelectProduct: () -> Unit,
    onNavigateToDetailTransaction: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tag = "Buat Pesanan Content"
    val stateRefresh = buatPesananViewModel.stateRefresh.observeAsState().value
    val stateCreate = buatPesananViewModel.stateCreate.observeAsState().value
    val errValidationCreateOrder = buatPesananViewModel.errValidationCreateOrder.observeAsState().value
    val errorState = buatPesananViewModel.errorState.observeAsState().value
    val errorMsg = buatPesananViewModel.errorMsg.observeAsState().value
    val isRefreshing by buatPesananViewModel.isRefreshing.collectAsState(initial = false)
    val message = buatPesananViewModel.message.observeAsState().value
    val statusCode = buatPesananViewModel.statusCode.observeAsState().value
    val payment = buatPesananViewModel.payment.asLiveData().observeAsState().value
    val selectedCustomer = buatPesananViewModel.selectedCustomer.observeAsState().value
    val dataOrderId = buatPesananViewModel.dataOrderId.observeAsState().value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { buatPesananViewModel.refresh() })

    val selectedOption = rememberSaveable { mutableStateOf(payment ?: paymentList[0].value) }
    val showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    val showErrorDialog = rememberSaveable { mutableStateOf(false) }
    val showErrorValidationDialog = rememberSaveable { mutableStateOf(false) }
    val msgErrorValidation = rememberSaveable { mutableStateOf("") }

    LaunchedEffect(errorState) {
        if (errorState == true && !errorMsg.isNullOrEmpty()) {
            snackbarHostState.showSnackbar(message = errorMsg)
        }
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
            buatPesananViewModel.setStateRefresh(null)
        }

        StateResponse.SUCCESS -> {
            showLoadingDialog.value = false
            showErrorDialog.value = false
            buatPesananViewModel.setStateRefresh(null)
        }

        else -> {}
    }

    when (errValidationCreateOrder) {
        ErrValidationCreateOrder.ERR_CUSTOMER -> {
            msgErrorValidation.value = stringResource(R.string.err_order_customer)
            showErrorValidationDialog.value = true
            buatPesananViewModel.setErrValidationCreateOrder(null)
        }

        ErrValidationCreateOrder.ERR_PAYMENT -> {
            msgErrorValidation.value = stringResource(R.string.err_order_payment)
            showErrorValidationDialog.value = true
            buatPesananViewModel.setErrValidationCreateOrder(null)
        }

        ErrValidationCreateOrder.ERR_PRODUCT -> {
            msgErrorValidation.value = stringResource(R.string.err_order_product)
            showErrorValidationDialog.value = true
            buatPesananViewModel.setErrValidationCreateOrder(null)
        }

        else -> { showErrorValidationDialog.value = false }
    }

    when (stateCreate) {
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
            buatPesananViewModel.setStateCreate(null)
        }

        StateResponse.SUCCESS -> {
            showLoadingDialog.value = false
            showErrorDialog.value = false
            buatPesananViewModel.reset()
            onNavigateToDetailTransaction(dataOrderId!!)
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
                title = { Text(stringResource(R.string.create_order)) },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateBack()
                        keyboardController?.hide()
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
                        buatPesananViewModel.handleCreateOrder()
                        keyboardController?.hide()
                    }) {
                        Icon(
                            Icons.Default.Check,
                            stringResource(R.string.finish),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .verticalScroll(rememberScrollState())
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    })
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

            if (errorState == true && !errorMsg.isNullOrEmpty()) {
                HeaderError(modifier = modifier, message = errorMsg)
                Spacer(modifier = modifier.height(16.dp))
            }

            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Row(
                    modifier = modifier
                        .clickable { buatPesananViewModel.reset() }
                        .padding(horizontal = 8.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(id = R.string.clear),
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = modifier.width(16.dp))
                    Icon(
                        Icons.Default.DeleteSweep,
                        contentDescription = stringResource(R.string.clear_data),
                        tint = Color.Red
                    )
                }
            }
            SelectCustomer(
                customer = selectedCustomer,
                onSelectCustomer = { onNavigateToSelectCustomer() },
                modifier = modifier
            )
            SelectPayment(
                buatPesananViewModel = buatPesananViewModel,
                paymentList = paymentList,
                selectedOption = selectedOption,
                modifier = modifier
            )
            SelectProduct(
                buatPesananViewModel = buatPesananViewModel,
                onSelectProduct = { onNavigateToSelectProduct() },
                modifier = modifier
            )
            TotalPayment(buatPesananViewModel = buatPesananViewModel, modifier = modifier)
            Spacer(modifier = modifier.height(32.dp))

            if (showLoadingDialog.value) {
                LoadingDialog(showLoadingDialog, modifier)
            }

            if (showErrorDialog.value) {
                AlertErrorDialog(
                    message = message ?: stringResource(id = R.string.error_msg_default),
                    showDialog = showErrorDialog,
                    modifier = modifier
                )
            }

            if (showErrorValidationDialog.value) {
                AlertErrorDialog(
                    message = msgErrorValidation.value,
                    showDialog = showErrorDialog,
                    modifier = modifier
                )
            }
        }
    }
}

private val paymentList = listOf(
    FilterOption("Tempo", "PENDING"),
    FilterOption("Tunai", "PAID")
)

@Preview(showBackground = true)
@Composable
private fun BuatPesananScreenPreview() {
    JJSembakoTheme {
        BuatPesananScreen(
            onNavigateBack = {},
            onNavigateToSelectCustomer = {},
            onNavigateToSelectProduct = {},
            onNavigateToDetailTransaction = {}
        )
    }
}