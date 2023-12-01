package com.dr.jjsembako.ui.feature_customer.detail

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dr.jjsembako.R
import com.dr.jjsembako.core.domain.model.Customer
import com.dr.jjsembako.core.model.FilterOption
import com.dr.jjsembako.ui.components.BottomSheetOrder
import com.dr.jjsembako.ui.components.CustomerInfo
import com.dr.jjsembako.ui.components.ErrorScreen
import com.dr.jjsembako.ui.components.SearchFilter
import com.dr.jjsembako.ui.feature_customer.components.CustomerButtonInfo
import com.dr.jjsembako.ui.theme.JJSembakoTheme
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun DetailPelangganScreen(
    idCust: String,
    onNavigateBack: () -> Unit,
    onNavigateToEditCust: (String) -> Unit,
    openMaps: (String) -> Unit,
    call: (String) -> Unit,
    chatWA: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isRandomTrue = Random.nextBoolean()

    if (isRandomTrue) {
        DetailPelangganContent(
            cust = dummy,
            onNavigateBack = { onNavigateBack() },
            onNavigateToEditCust = { id -> onNavigateToEditCust(id) },
            openMaps = { url -> openMaps(url) },
            call = { uri -> call(uri) },
            chatWA = { url -> chatWA(url) },
            modifier = modifier
        )
    } else {
        ErrorScreen(onNavigateBack = { onNavigateBack() }, modifier = modifier)
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun DetailPelangganContent(
    cust: Customer,
    onNavigateBack: () -> Unit,
    onNavigateToEditCust: (String) -> Unit,
    openMaps: (String) -> Unit,
    call: (String) -> Unit,
    chatWA: (String) -> Unit,
    modifier: Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    var menuExpanded by remember { mutableStateOf(false) }

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
                            onClick = { onNavigateToEditCust(cust.id) },)
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.delete)) },
                            onClick = { /*TODO*/ })
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
                    onClick = { keyboardController?.hide() })
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
        }
    }
}

private val dummy = Customer(
    "abcd-123",
    "Bambang",
    "Toko Makmur",
    "Jl. Nusa Indah 3, Belimbing, Jambu, Sayuran, Tumbuhan",
    "https://maps.app.goo.gl/MQBEcptYvdYfBaNc9",
    "081234567890",
    1_500_000L
)

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