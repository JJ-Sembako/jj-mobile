package com.dr.jjsembako.performa.presentation.home

import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.presentation.components.dialog.AlertErrorDialog
import com.dr.jjsembako.core.presentation.components.dialog.LoadingDialog
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.formatDateString
import com.dr.jjsembako.core.utils.getCurrentYearMonthInGmt7
import com.dr.jjsembako.core.utils.initializeDateValues
import com.dr.jjsembako.performa.presentation.components.home.HistorySection
import com.dr.jjsembako.performa.presentation.components.home.MenuSection
import com.dr.jjsembako.performa.presentation.components.home.OmzetSection
import com.dr.jjsembako.performa.presentation.components.home.StatisticSection
import com.dr.jjsembako.performa.presentation.components.home.WelcomeSection
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    username: String = "username",
    context: Context,
    clipboardManager: ClipboardManager,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCreateOrder: () -> Unit,
    onNavigateToWarehouse: () -> Unit,
    onNavigateToCustomer: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToPerformance: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onLogout: () -> Unit,
    backHandler: () -> Unit
) {
    val tag = "Home-S"
    val viewModel: HomeViewModel = hiltViewModel()
    val stateFirst = viewModel.stateFirst.observeAsState().value
    val stateSecond = viewModel.stateSecond.observeAsState().value
    val stateThird = viewModel.stateThird.observeAsState().value
    val stateFourth = viewModel.stateFourth.observeAsState().value
    val stateRefresh = viewModel.stateRefresh.observeAsState().value
    val isRefreshing by viewModel.isRefreshing.collectAsState(initial = false)
    val statusCode = viewModel.statusCode.observeAsState().value
    val message = viewModel.message.observeAsState().value
    val dataOrders = viewModel.dataOrders.observeAsState().value
    val totalOrders = viewModel.totalOrders.observeAsState().value
    val dataOmzetMonthly = viewModel.dataOmzetMonthly.observeAsState().value
    val totalSelledProductMonthly = viewModel.totalSelledProductMonthly.observeAsState().value

    val scrollState = rememberScrollState()
    val thisYear = rememberSaveable { mutableIntStateOf(0) }
    val thisMonth = rememberSaveable { mutableIntStateOf(0) }
    val minDate = rememberSaveable { mutableStateOf("") }
    val maxDate = rememberSaveable { mutableStateOf("") }
    val isErrorInit = remember { mutableStateOf(false) }
    val showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    val showErrorDialog = remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refresh() })

    BackHandler { backHandler() }

    LaunchedEffect(Unit) {
        if (thisYear.intValue == 0) {
            val time = getCurrentYearMonthInGmt7()
            initializeDateValues(minDate, maxDate)
            thisYear.intValue = time[0]
            thisMonth.intValue = time[1]
            viewModel.setTime(
                thisMonth.intValue, thisYear.intValue,
                formatDateString(minDate.value), formatDateString(maxDate.value)
            )
        } else viewModel.refresh()
    }

    when (stateFirst) {
        StateResponse.ERROR -> {
            Log.e(tag, "Error-first")
            Log.e(tag, "state: $stateFirst")
            Log.e(tag, "Error: $message")
            Log.e(tag, "statusCode: $statusCode")
        }

        else -> {}
    }

    when (stateSecond) {
        StateResponse.ERROR -> {
            Log.e(tag, "Error-second")
            Log.e(tag, "state: $stateSecond")
            Log.e(tag, "Error: $message")
            Log.e(tag, "statusCode: $statusCode")
        }

        else -> {}
    }

    when (stateThird) {
        StateResponse.ERROR -> {
            Log.e(tag, "Error-third")
            Log.e(tag, "state: $stateThird")
            Log.e(tag, "Error: $message")
            Log.e(tag, "statusCode: $statusCode")
        }

        else -> {}
    }

    when (stateFourth) {
        StateResponse.LOADING -> {
            showLoadingDialog.value = true
        }

        StateResponse.ERROR -> {
            Log.e(tag, "Error-fourth")
            Log.e(tag, "state: $stateFourth")
            Log.e(tag, "Error: $message")
            Log.e(tag, "statusCode: $statusCode")
            showLoadingDialog.value = false
            showErrorDialog.value = true
            isErrorInit.value = true
            viewModel.setStateFourth(null)
        }

        StateResponse.SUCCESS -> {
            showLoadingDialog.value = false
            showErrorDialog.value = false
            isErrorInit.value = false
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
            .verticalScroll(scrollState),
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = modifier.fillMaxWidth()) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(224.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Column(
                    modifier = modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = modifier.height(16.dp))
                    WelcomeSection(name = username, onLogout = { onLogout() }, modifier = modifier)
                    Spacer(modifier = modifier.height(8.dp))
                    OmzetSection(
                        omzet = dataOmzetMonthly?.omzet ?: 0L,
                        isErrorInit = isErrorInit.value,
                        modifier = modifier
                    )
                    Spacer(modifier = modifier.height(8.dp))
                    StatisticSection(
                        totalPesanan = totalOrders ?: 0,
                        totalBarang = totalSelledProductMonthly ?: 0,
                        isErrorInit = isErrorInit.value,
                        modifier = modifier
                    )
                }
            }
            Column {
                Spacer(modifier = modifier.height(16.dp))
                MenuSection(
                    onNavigateToCreateOrder = { onNavigateToCreateOrder() },
                    onNavigateToWarehouse = { onNavigateToWarehouse() },
                    onNavigateToCustomer = { onNavigateToCustomer() },
                    onNavigateToHistory = { onNavigateToHistory() },
                    onNavigateToPerformance = { onNavigateToPerformance() },
                    onNavigateToSetting = { onNavigateToSetting() },
                    modifier = modifier
                )
                Spacer(modifier = modifier.height(24.dp))
                HistorySection(
                    isErrorInit = isErrorInit.value,
                    dataOrders = dataOrders,
                    context = context,
                    clipboardManager = clipboardManager,
                    onNavigateToDetail = { id -> onNavigateToDetail(id) },
                    modifier = modifier
                )
            }

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
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = modifier.align(Alignment.TopCenter)
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    JJSembakoTheme {
        HomeScreen(
            context = LocalContext.current,
            clipboardManager = LocalClipboardManager.current,
            onNavigateToDetail = {},
            onNavigateToCreateOrder = {},
            onNavigateToWarehouse = {},
            onNavigateToCustomer = {},
            onNavigateToHistory = {},
            onNavigateToPerformance = {},
            onNavigateToSetting = {},
            onLogout = {},
            backHandler = {}
        )
    }
}