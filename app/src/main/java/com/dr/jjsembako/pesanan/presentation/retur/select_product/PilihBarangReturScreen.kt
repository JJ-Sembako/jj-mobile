package com.dr.jjsembako.pesanan.presentation.retur.select_product

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dr.jjsembako.R
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.presentation.components.bottom_sheet.BottomSheetProduct
import com.dr.jjsembako.core.presentation.components.dialog.AlertErrorDialog
import com.dr.jjsembako.core.presentation.components.dialog.LoadingDialog
import com.dr.jjsembako.core.presentation.components.dialog.PreviewImageDialog
import com.dr.jjsembako.core.presentation.components.screen.ErrorScreen
import com.dr.jjsembako.core.presentation.components.screen.LoadingScreen
import com.dr.jjsembako.core.presentation.components.screen.NotFoundScreen
import com.dr.jjsembako.core.presentation.components.utils.SearchFilter
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.rememberMutableStateListOf
import com.dr.jjsembako.core.utils.rememberMutableStateMapOf
import com.dr.jjsembako.pesanan.presentation.components.card.SelectOrderRCard
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@Composable
fun PilihBarangReturScreen(
    id: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tag = "PB-Retur-S"
    val viewModel: PilihBarangReturViewModel = hiltViewModel()
    val state = viewModel.state.observeAsState().value
    val statusCode = viewModel.statusCode.observeAsState().value
    val message = viewModel.message.observeAsState().value
    val orderData = viewModel.orderData

    // Set id for the first time Composable is rendered
    LaunchedEffect(id, Unit) {
        viewModel.setId(id)
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
                PilihBarangReturContent(
                    viewModel = viewModel,
                    onNavigateBack = { onNavigateBack() },
                    modifier = modifier
                )
            }
        }

        else -> {}
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun PilihBarangReturContent(
    viewModel: PilihBarangReturViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier
) {
    val tag = "PB-Retur-C"
    val stateRefresh = viewModel.stateRefresh.observeAsState().value
    val isRefreshing by viewModel.isRefreshing.collectAsState(initial = false)
    val statusCode = viewModel.statusCode.observeAsState().value
    val message = viewModel.message.observeAsState().value
    val option = viewModel.dataCategories.observeAsState().value
    val productOrder = viewModel.productOrder.observeAsState().value
    val selectedData = viewModel.selectedData.observeAsState().value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    val showErrorDialog = remember { mutableStateOf(false) }
    val showPreviewImageDialog = remember { mutableStateOf(false) }
    val showSheet = remember { mutableStateOf(false) }
    val activeSearch = remember { mutableStateOf(false) }
    val previewProductName = remember { mutableStateOf("") }
    val previewProductImage = remember { mutableStateOf("") }
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val checkBoxResult = rememberMutableStateListOf<String>()
    val checkBoxStates = rememberMutableStateMapOf<String, Boolean>()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refresh() })

    LaunchedEffect(Unit) {
        if (!option.isNullOrEmpty()) {
            if (checkBoxResult.isEmpty()) {
                checkBoxResult.addAll(option.map { it!!.value })
                checkBoxStates.putAll(option.map { it!!.value to true })
            }
        }
    }

    LaunchedEffect(option) {
        if (!option.isNullOrEmpty()) {
            if (checkBoxResult.isEmpty()) {
                checkBoxResult.addAll(option.map { it!!.value })
                checkBoxStates.putAll(option.map { it!!.value to true })
            } else {
                val newOption = option.filterNot { checkBoxResult.contains(it!!.value) }
                checkBoxResult.addAll(newOption.map { it!!.value })
                checkBoxStates.putAll(newOption.map { it!!.value to true })
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
                title = { Text(stringResource(R.string.select_product)) },
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
                        viewModel.saveData()
                        onNavigateBack()
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
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            keyboardController?.hide()
                            activeSearch.value = false
                            focusManager.clearFocus()
                        })
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchFilter(
                    placeholder = stringResource(R.string.search_product),
                    activeSearch,
                    searchQuery,
                    searchFunction = { },
                    openFilter = { showSheet.value = !showSheet.value },
                    modifier = modifier
                )
                Spacer(modifier = modifier.height(16.dp))

                val filteredOrders = productOrder?.filter { order ->
                    order!!.product.name.contains(searchQuery.value, ignoreCase = true) &&
                            checkBoxResult.isNotEmpty() && checkBoxResult.contains(order.product.category)
                }

                if (filteredOrders != null) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedData == null) stringResource(R.string.select_not) else stringResource(
                                R.string.select_already
                            ),
                            fontSize = 16.sp, fontWeight = FontWeight.Light,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )
                        Spacer(modifier = modifier.width(16.dp))
                        Icon(
                            Icons.Default.DeleteSweep,
                            contentDescription = stringResource(R.string.clear_data),
                            tint = Color.Red,
                            modifier = modifier
                                .size(32.dp)
                                .clickable { viewModel.reset() }
                        )
                    }
                    Spacer(modifier = modifier.height(16.dp))

                    if (filteredOrders.isNotEmpty()) {
                        LazyColumn(
                            modifier = modifier
                                .fillMaxWidth()
                        ) {
                            items(items = filteredOrders, key = { order ->
                                order?.id ?: "empty-${System.currentTimeMillis()}"
                            }, itemContent = { order ->
                                if (order != null) {
                                    SelectOrderRCard(
                                        viewModel, order, showPreviewImageDialog,
                                        previewProductName, previewProductImage, modifier
                                    )
                                }
                                Spacer(modifier = modifier.height(8.dp))
                            })
                        }
                    } else {
                        NotFoundScreen(modifier = modifier)
                    }
                } else NotFoundScreen(modifier = modifier)

                if (showSheet.value) {
                    BottomSheetProduct(
                        optionList = option,
                        checkBoxResult = checkBoxResult,
                        checkBoxStates = checkBoxStates,
                        showSheet = showSheet,
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
private fun PilihBarangReturScreenPreview() {
    JJSembakoTheme {
        PilihBarangReturScreen(
            id = "123",
            onNavigateBack = {}
        )
    }
}