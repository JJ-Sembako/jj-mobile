package com.dr.jjsembako.feature_customer.presentation.detail

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import com.dr.jjsembako.core.presentation.components.AlertDeleteDialog
import com.dr.jjsembako.core.presentation.components.AlertErrorDialog
import com.dr.jjsembako.core.presentation.components.BottomSheetOrder
import com.dr.jjsembako.core.presentation.components.CustomerInfo
import com.dr.jjsembako.core.presentation.components.ErrorScreen
import com.dr.jjsembako.core.presentation.components.LoadingDialog
import com.dr.jjsembako.core.presentation.components.LoadingScreen
import com.dr.jjsembako.core.presentation.components.SearchFilter
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_customer.presentation.components.CustomerButtonInfo
import kotlinx.coroutines.launch

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
    val detailPelangganViewModel: DetailPelangganViewModel = hiltViewModel()
    val stateFirst = detailPelangganViewModel.stateFirst.observeAsState().value
    val statusCode = detailPelangganViewModel.statusCode.observeAsState().value
    val message = detailPelangganViewModel.message.observeAsState().value
    val customerData = detailPelangganViewModel.customerData

    // Set id for the first time Composable is rendered
    LaunchedEffect(idCust) {
        detailPelangganViewModel.setId(idCust)
    }

    when (stateFirst) {
        StateResponse.LOADING -> {
            LoadingScreen(modifier = modifier)
        }

        StateResponse.ERROR -> {
            Log.e("DetailPelanggan", "Error")
            Log.e("DetailPelanggan", "state: $stateFirst")
            Log.e("DetailPelanggan", "Error: $message")
            Log.e("DetailPelanggan", "statusCode: $statusCode")
            ErrorScreen(
                onNavigateBack = { onNavigateBack() },
                onReload = {
                    detailPelangganViewModel.fetchDetailCustomer(idCust)
                },
                message = message ?: "Unknown error",
                modifier = modifier
            )
        }

        StateResponse.SUCCESS -> {
            if (customerData == null) {
                ErrorScreen(
                    onNavigateBack = { onNavigateBack() },
                    onReload = { detailPelangganViewModel.fetchDetailCustomer(idCust) },
                    message = "Server Error",
                    modifier = modifier
                )
            } else {
                DetailPelangganContent(
                    cust = customerData,
                    detailPelangganViewModel = detailPelangganViewModel,
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
    detailPelangganViewModel: DetailPelangganViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEditCust: () -> Unit,
    openMaps: (String) -> Unit,
    call: (String) -> Unit,
    chatWA: (String) -> Unit,
    modifier: Modifier
) {
    val stateSecond = detailPelangganViewModel.stateSecond.observeAsState().value
    val statusCode = detailPelangganViewModel.statusCode.observeAsState().value
    val message = detailPelangganViewModel.message.observeAsState().value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    var showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    var showErrorDialog = rememberSaveable { mutableStateOf(false) }

    var menuExpanded by remember { mutableStateOf(false) }
    var showDialog = remember { mutableStateOf(false) }
    var showSheet = remember { mutableStateOf(false) }
    var (selectedOptionPayment, onOptionSelectedPayment) = remember {
        mutableStateOf(
            radioOptionsPayment[0]
        )
    }
    var (selectedOptionOrder, onOptionSelectedOrder) = remember { mutableStateOf(radioOptionsOrder[0]) }
    var searchQuery = remember { mutableStateOf("") }
    var activeSearch = remember { mutableStateOf(false) }

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
            Log.e("DetailPelanggan", "Error")
            Log.e("DetailPelanggan", "state: $stateSecond")
            Log.e("DetailPelanggan", "Error: $message")
            Log.e("DetailPelanggan", "statusCode: $statusCode")
            showLoadingDialog.value = false
            showErrorDialog.value = true
            detailPelangganViewModel.setStateSecond(null)
        }

        StateResponse.SUCCESS -> {
            showLoadingDialog.value = false
            showErrorDialog.value = false
            detailPelangganViewModel.setStateSecond(null)
            onNavigateBack()
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
                    onNavigateBack = { onNavigateBack() },
                    onDelete = { detailPelangganViewModel.handleDeleteCustomer(cust.id) },
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
fun DetailPelangganScreenPreview() {
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