package com.dr.jjsembako.feature_customer.presentation.detail

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dr.jjsembako.R
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.core.presentation.components.bottom_sheet.BottomSheetOrder
import com.dr.jjsembako.core.presentation.components.card.CustomerInfo
import com.dr.jjsembako.core.presentation.components.dialog.AlertDeleteDialog
import com.dr.jjsembako.core.presentation.components.dialog.AlertErrorDialog
import com.dr.jjsembako.core.presentation.components.dialog.LoadingDialog
import com.dr.jjsembako.core.presentation.components.screen.ErrorScreen
import com.dr.jjsembako.core.presentation.components.screen.LoadingScreen
import com.dr.jjsembako.core.presentation.components.utils.SearchFilter
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_customer.presentation.components.CustomerButtonInfo
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun DetailPelangganScreen(
    idCust: String,
    onNavigateBack: () -> Unit,
    onNavigateToEditCust: () -> Unit,
    openMaps: (String) -> Unit,
    call: (String) -> Unit,
    chatWA: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tag = "DetailPelanggan-S"
    val viewModel: DetailPelangganViewModel = hiltViewModel()
    val stateFirst = viewModel.stateFirst.observeAsState().value
    val statusCode = viewModel.statusCode.observeAsState().value
    val message = viewModel.message.observeAsState().value
    val customerData = viewModel.customerData

    // Set id for the first time Composable is rendered
    LaunchedEffect(idCust) {
        viewModel.setId(idCust)
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
                onReload = {
                    viewModel.fetchDetailCustomer(idCust)
                },
                message = message ?: "Unknown error",
                modifier = modifier
            )
        }

        StateResponse.SUCCESS -> {
            if (customerData == null) {
                ErrorScreen(
                    onNavigateBack = { onNavigateBack() },
                    onReload = { viewModel.fetchDetailCustomer(idCust) },
                    message = "Server Error",
                    modifier = modifier
                )
            } else {
                DetailPelangganContent(
                    cust = customerData,
                    viewModel = viewModel,
                    onNavigateBack = { onNavigateBack() },
                    onNavigateToEditCust = { onNavigateToEditCust() },
                    openMaps = { url -> openMaps(url) },
                    call = { uri -> call(uri) },
                    chatWA = { url -> chatWA(url) },
                    modifier = modifier
                )
            }
        }

        else -> {}
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun DetailPelangganContent(
    cust: DataCustomer,
    viewModel: DetailPelangganViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEditCust: () -> Unit,
    openMaps: (String) -> Unit,
    call: (String) -> Unit,
    chatWA: (String) -> Unit,
    modifier: Modifier
) {
    val tag = "DetailPelanggan-C"
    val stateSecond = viewModel.stateSecond.observeAsState().value
    val stateRefresh = viewModel.stateRefresh.observeAsState().value
    val statusCode = viewModel.statusCode.observeAsState().value
    val message = viewModel.message.observeAsState().value
    val isRefreshing by viewModel.isRefreshing.collectAsState(initial = false)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    val showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    val showErrorDialog = rememberSaveable { mutableStateOf(false) }

    var menuExpanded by remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val showSheet = remember { mutableStateOf(false) }
    val (selectedOptionPayment, onOptionSelectedPayment) = remember {
        mutableStateOf(
            radioOptionsPayment[0]
        )
    }
    val (selectedOptionOrder, onOptionSelectedOrder) = remember { mutableStateOf(radioOptionsOrder[0]) }
    val searchQuery = remember { mutableStateOf("") }
    val activeSearch = remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refresh() })

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_empty))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )

    LaunchedEffect(key1 = keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }

    when (stateSecond) {
        StateResponse.LOADING -> {
            LoadingScreen(modifier = modifier)
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
            viewModel.setStateSecond(null)
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
                title = { Text(stringResource(R.string.det_cust)) },
                navigationIcon = {
                    IconButton(onClick = {
                        keyboardController?.hide()
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
                    IconButton(onClick = { menuExpanded = !menuExpanded }) {
                        Icon(
                            Icons.Default.MoreVert,
                            stringResource(R.string.menu),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    DropdownMenu(
                        modifier = modifier.width(144.dp),
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.edit)) },
                            onClick = { onNavigateToEditCust() },
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.delete)) },
                            onClick = { showDialog.value = true })
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
        ) {
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = modifier
                    .fillMaxWidth()
                    .height((pullRefreshState.progress * 100).roundToInt().dp)
            )
            Column(
                modifier = modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            keyboardController?.hide()
                            activeSearch.value = false
                            focusManager.clearFocus()
                        })
                    .padding(contentPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomerInfo(
                    onNavigateToDetailCust = {},
                    customer = cust,
                    modifier = modifier
                )
                CustomerButtonInfo(
                    cust = cust,
                    openMaps = { url -> openMaps(url) },
                    call = { uri -> call(uri) },
                    chatWA = { url -> chatWA(url) },
                    modifier = modifier
                )
                Spacer(modifier = modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.order_history),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = modifier.height(16.dp))
                SearchFilter(
                    placeholder = stringResource(R.string.search_cust),
                    activeSearch,
                    searchQuery,
                    searchFunction = {},
                    openFilter = { showSheet.value = !showSheet.value },
                    modifier = modifier
                )
                Spacer(modifier = modifier.height(16.dp))
                LottieAnimation(
                    enableMergePaths = true,
                    composition = composition,
                    progress = { progress },
                    modifier = modifier.size(150.dp)
                )
                Spacer(modifier = modifier.height(48.dp))
                if (showSheet.value) {
                    BottomSheetOrder(
                        optionListPayment = radioOptionsPayment,
                        optionListOrder = radioOptionsOrder,
                        selectedOptionPayment = selectedOptionPayment,
                        selectedOptionOrder = selectedOptionOrder,
                        onOptionSelectedPayment = onOptionSelectedPayment,
                        onOptionSelectedOrder = onOptionSelectedOrder,
                        showSheet = showSheet,
                        modifier = modifier
                    )
                }
                if (showDialog.value) {
                    AlertDeleteDialog(
                        onDelete = { viewModel.handleDeleteCustomer(cust.id) },
                        showDialog = showDialog,
                        modifier = modifier
                    )
                }

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
            }
        }
    }
}

private val radioOptionsPayment = listOf(
    FilterOption("Semua", "semua"),
    FilterOption("Belum Lunas", "belum-lunas"),
    FilterOption("Lunas", "lunas")
)

private val radioOptionsOrder = listOf(
    FilterOption("Semua", "semua"),
    FilterOption("Menunggu Konfirmasi", "menunggu-konfirmasi"),
    FilterOption("Dikemas", "dikemas"),
    FilterOption("Dikirim", "dikirim"),
    FilterOption("Selesai", "selesai")
)

@Composable
@Preview(showBackground = true)
private fun DetailPelangganScreenPreview() {
    JJSembakoTheme {
        DetailPelangganScreen(
            idCust = "",
            onNavigateBack = {},
            onNavigateToEditCust = {},
            openMaps = {},
            call = {},
            chatWA = {}
        )
    }
}