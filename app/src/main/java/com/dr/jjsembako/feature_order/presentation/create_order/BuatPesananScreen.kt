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
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_order.presentation.components.SelectCustomer
import com.dr.jjsembako.feature_order.presentation.components.SelectPayment
import com.dr.jjsembako.feature_order.presentation.components.SelectProduct
import com.dr.jjsembako.feature_order.presentation.components.TotalPayment
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun BuatPesananScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSelectCustomer: () -> Unit,
    onNavigateToSelectProduct: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tag = "Buat Pesanan Screen"
    val buatPesananViewModel: BuatPesananViewModel = hiltViewModel()
    val state = buatPesananViewModel.state.observeAsState().value
    val stateRefresh = buatPesananViewModel.stateRefresh.observeAsState().value
    val isRefreshing by buatPesananViewModel.isRefreshing.collectAsState(initial = false)
    val message = buatPesananViewModel.message.observeAsState().value
    val statusCode = buatPesananViewModel.statusCode.observeAsState().value
    val idCust = buatPesananViewModel.idCustomer.asLiveData().observeAsState().value
    val payment = buatPesananViewModel.payment.asLiveData().observeAsState().value
    val selectedCustomer = buatPesananViewModel.selectedCustomer.observeAsState().value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val selectedOption = rememberSaveable { mutableIntStateOf(payment ?: 0) }

    val showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    val showErrorDialog = rememberSaveable { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { buatPesananViewModel.refresh() })

    LaunchedEffect(idCust) {
        Log.e("DataStore-buat", "idCust: $idCust")
    }
    LaunchedEffect(payment) {
        Log.e("DataStore-buat", "payment: $payment")
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
                        onNavigateBack()
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
        }
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
            SelectProduct(onSelectProduct = { onNavigateToSelectProduct() }, modifier = modifier)
            TotalPayment(totalPrice = 1525750, modifier = modifier)
            Spacer(modifier = modifier.height(32.dp))
        }
    }
}

private val paymentList = listOf(
    FilterOption("Tempo", "0"),
    FilterOption("Tunai", "1")
)

@Preview(showBackground = true)
@Composable
private fun BuatPesananScreenPreview() {
    JJSembakoTheme {
        BuatPesananScreen(
            onNavigateBack = {},
            onNavigateToSelectCustomer = {},
            onNavigateToSelectProduct = {}
        )
    }
}