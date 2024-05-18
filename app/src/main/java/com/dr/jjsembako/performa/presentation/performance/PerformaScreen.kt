package com.dr.jjsembako.performa.presentation.performance

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.dr.jjsembako.core.presentation.components.dialog.AlertErrorDialog
import com.dr.jjsembako.core.presentation.components.dialog.LoadingDialog
import com.dr.jjsembako.core.presentation.components.screen.ErrorScreen
import com.dr.jjsembako.core.presentation.components.screen.LoadingScreen
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.formatRupiah
import com.dr.jjsembako.core.utils.getCurrentYearMonthInGmt7
import com.dr.jjsembako.core.utils.labelPerformance
import com.dr.jjsembako.performa.presentation.components.performance.HeaderMonthYearSection
import com.dr.jjsembako.performa.presentation.components.performance.MonthYearPickerDialog
import com.dr.jjsembako.performa.presentation.components.performance.OmzetChartSection
import com.dr.jjsembako.performa.presentation.components.performance.SelledProductCard
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@Composable
fun PerformaScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tag = "Performa-S"
    val viewModel: PerformaViewModel = hiltViewModel()
    val stateFirst = viewModel.stateFirst.observeAsState().value
    val stateSecond = viewModel.stateSecond.observeAsState().value
    val stateThird = viewModel.stateThird.observeAsState().value
    val statusCode = viewModel.statusCode.observeAsState().value
    val message = viewModel.message.observeAsState().value

    val selectedYear = rememberSaveable { mutableIntStateOf(0) }
    val selectedMonth = rememberSaveable { mutableIntStateOf(0) }
    val thisYear = rememberSaveable { mutableIntStateOf(0) }
    val maxRange = 10

    LaunchedEffect(Unit) {
        if (thisYear.intValue == 0) {
            val time = getCurrentYearMonthInGmt7()
            selectedYear.intValue = time[0]
            thisYear.intValue = time[0]
            selectedMonth.intValue = time[1]
            viewModel.setTime(selectedMonth.intValue, selectedYear.intValue)
        }
    }

    when (stateFirst) {
        StateResponse.LOADING -> {
            LoadingScreen(modifier = modifier)
        }

        StateResponse.ERROR -> {
            Log.e(tag, "Error-first")
            Log.e(tag, "state: $stateFirst")
            Log.e(tag, "Error: $message")
            Log.e(tag, "statusCode: $statusCode")
            ErrorScreen(
                onNavigateBack = { onNavigateBack() },
                onReload = { viewModel.refresh() },
                message = message ?: "Unknown error",
                modifier = modifier
            )
        }

        StateResponse.SUCCESS -> {
            when (stateSecond) {
                StateResponse.LOADING -> {
                    LoadingScreen(modifier = modifier)
                }

                StateResponse.ERROR -> {
                    Log.e(tag, "Error-second")
                    Log.e(tag, "state: $stateSecond")
                    Log.e(tag, "Error: $message")
                    Log.e(tag, "statusCode: $statusCode")
                    ErrorScreen(
                        onNavigateBack = { onNavigateBack() },
                        onReload = { viewModel.refresh() },
                        message = message ?: "Unknown error",
                        modifier = modifier
                    )
                }

                StateResponse.SUCCESS -> {
                    when (stateThird) {
                        StateResponse.LOADING -> {
                            LoadingScreen(modifier = modifier)
                        }

                        StateResponse.ERROR -> {
                            Log.e(tag, "Error-third")
                            Log.e(tag, "state: $stateSecond")
                            Log.e(tag, "Error: $message")
                            Log.e(tag, "statusCode: $statusCode")
                            ErrorScreen(
                                onNavigateBack = { onNavigateBack() },
                                onReload = { viewModel.refresh() },
                                message = message ?: "Unknown error",
                                modifier = modifier
                            )
                        }

                        StateResponse.SUCCESS -> {
                            PerformaContent(
                                selectedYear = selectedYear,
                                selectedMonth = selectedMonth,
                                thisYear = thisYear,
                                maxRange = maxRange,
                                viewModel = viewModel,
                                onNavigateBack = { onNavigateBack() },
                                modifier = modifier
                            )
                        }

                        else -> {}
                    }
                }

                else -> {}
            }
        }

        else -> {}
    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PerformaContent(
    selectedYear: MutableState<Int>,
    selectedMonth: MutableState<Int>,
    thisYear: MutableState<Int>,
    maxRange: Int,
    viewModel: PerformaViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier
) {
    val tag = "Performa-C"
    val stateRefresh = viewModel.stateRefresh.observeAsState().value
    val isRefreshing by viewModel.isRefreshing.collectAsState(initial = false)
    val message = viewModel.message.observeAsState().value
    val statusCode = viewModel.statusCode.observeAsState().value
    val dataOmzet = viewModel.dataOmzet.observeAsState().value
    val dataOmzetMonthly = viewModel.dataOmzetMonthly.observeAsState().value
    val dataSelledProductMonthly = viewModel.dataSelledProductMonthly.observeAsState().value
    val totalSelledProductMonthly = viewModel.totalSelledProductMonthly.observeAsState(Int).value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val isTimeChange = rememberSaveable { mutableStateOf(false) }
    val showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    val showErrorDialog = remember { mutableStateOf(false) }
    val showYearPickerDialog = rememberSaveable { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_empty))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refresh() })

    val month = listOf(
        stringResource(R.string.month_01), stringResource(R.string.month_02),
        stringResource(R.string.month_03), stringResource(R.string.month_04),
        stringResource(R.string.month_05), stringResource(R.string.month_06),
        stringResource(R.string.month_07), stringResource(R.string.month_08),
        stringResource(R.string.month_09), stringResource(R.string.month_10),
        stringResource(R.string.month_11), stringResource(R.string.month_12),
    )

    LaunchedEffect(Unit){
        showLoadingDialog.value = false
        showErrorDialog.value = false
        viewModel.setStateRefresh(null)
    }

    LaunchedEffect(selectedYear.value, selectedMonth.value) {
        if (isTimeChange.value) {
            when (isRefreshing) {
                false -> {
                    viewModel.setTime(selectedMonth.value, selectedYear.value)
                    viewModel.setStateRefresh(null)
                }

                else -> {}
            }
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
                title = { Text(stringResource(R.string.performance)) },
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
            ) {
                Text(
                    text = stringResource(R.string.omzet_last_year), fontWeight = FontWeight.Bold,
                    fontSize = 18.sp, textAlign = TextAlign.Center,
                    modifier = modifier
                        .wrapContentSize(Alignment.Center)
                        .padding(top = 16.dp)
                )
                Spacer(modifier = modifier.height(32.dp))
                if (!dataOmzet.isNullOrEmpty()) {
                    OmzetChartSection(
                        omzet = dataOmzet,
                        time = labelPerformance(dataOmzet, month),
                        modifier = modifier
                    )
                } else {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .height(240.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LottieAnimation(
                            enableMergePaths = true,
                            composition = composition,
                            progress = { progress },
                            modifier = modifier.size(80.dp)
                        )
                        Spacer(modifier = modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.no_omzet_data),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = modifier.height(32.dp))
                HeaderMonthYearSection(
                    thisYear = thisYear.value,
                    maxRange = maxRange,
                    isTimeChange = isTimeChange,
                    selectedYear = selectedYear,
                    selectedMonth = selectedMonth,
                    showDialog = showYearPickerDialog,
                    modifier = modifier
                )
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(
                            R.string.omzet_monthly,
                            formatRupiah(dataOmzetMonthly?.omzet ?: 0L)
                        ), fontSize = 12.sp, fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(
                            R.string.total_selled_monthly,
                            totalSelledProductMonthly
                        ), fontSize = 12.sp, fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = modifier.height(12.dp))
                    dataSelledProductMonthly?.let {
                        if (it.isNotEmpty()) {
                            it.forEach { data ->
                                if (data != null) SelledProductCard(data, modifier)
                                Spacer(modifier = modifier.height(8.dp))
                            }
                        }
                    }
                }
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

                if (showYearPickerDialog.value) {
                    MonthYearPickerDialog(
                        thisYear = thisYear.value,
                        maxRange = maxRange,
                        isTimeChange = isTimeChange,
                        selectedYear = selectedYear,
                        selectedMonth = selectedMonth,
                        showDialog = showYearPickerDialog,
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
private fun PerformaScreenPreview() {
    JJSembakoTheme {
        PerformaScreen(
            onNavigateBack = {},
        )
    }
}