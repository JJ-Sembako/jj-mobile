package com.dr.jjsembako.feature_history.presentation.list

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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.remote.response.order.OrderDataItem
import com.dr.jjsembako.core.presentation.components.bottom_sheet.BottomSheetOrderHistory
import com.dr.jjsembako.core.presentation.components.card.OrderHistoryCard
import com.dr.jjsembako.core.presentation.components.screen.ErrorScreen
import com.dr.jjsembako.core.presentation.components.screen.LoadingScreen
import com.dr.jjsembako.core.presentation.components.screen.NotFoundScreen
import com.dr.jjsembako.core.presentation.components.utils.SearchFilter
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.DataMapper.mapOrderDataItemToDataOrderHistoryCard
import com.dr.jjsembako.core.utils.formatDateString
import com.dr.jjsembako.core.utils.initializeDateValues

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RiwayatScreen(
    context: Context,
    clipboardManager: ClipboardManager,
    onNavigateToDetail: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tag = "RiwayatScreen"
    val viewModel: RiwayatViewModel = hiltViewModel()
    val orderPagingItems: LazyPagingItems<OrderDataItem> =
        viewModel.orderState.collectAsLazyPagingItems()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val showSheet = remember { mutableStateOf(false) }
    val isFilterOn = rememberSaveable { mutableStateOf(false) }
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val activeSearch = remember { mutableStateOf(false) }
    val fromDate = rememberSaveable { mutableStateOf("") }
    val untilDate = rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        initializeDateValues(fromDate, untilDate)
    }

    LaunchedEffect(searchQuery.value, fromDate.value, untilDate.value, isFilterOn.value) {
        if (searchQuery.value.isEmpty()) {
            if (isFilterOn.value) viewModel.fetchOrders(
                null,
                formatDateString(fromDate.value),
                formatDateString(untilDate.value)
            )
            else viewModel.fetchOrders(null, null, null)
        } else {
            if (isFilterOn.value) viewModel.fetchOrders(
                searchQuery.value,
                formatDateString(fromDate.value),
                formatDateString(untilDate.value)
            )
            else viewModel.fetchOrders(searchQuery.value, null, null)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = { Text(stringResource(R.string.history)) },
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
                        activeSearch.value = false
                        focusManager.clearFocus()
                    })
                .padding(contentPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchFilter(
                placeholder = stringResource(R.string.search_transaction),
                activeSearch,
                searchQuery,
                searchFunction = { },
                openFilter = { showSheet.value = !showSheet.value },
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))

            when (orderPagingItems.loadState.refresh) {
                LoadState.Loading -> {
                    LoadingScreen(modifier = modifier)
                }

                is LoadState.Error -> {
                    val error = orderPagingItems.loadState.refresh as LoadState.Error
                    Log.e(tag, "Error")
                    ErrorScreen(
                        onNavigateBack = { },
                        onReload = {
                            if (searchQuery.value.isNotEmpty()) {
                                if (isFilterOn.value) viewModel.fetchOrders(
                                    searchQuery.value,
                                    fromDate.value,
                                    untilDate.value
                                )
                                else viewModel.fetchOrders(searchQuery.value, null, null)
                            } else {
                                if (isFilterOn.value) viewModel.fetchOrders(
                                    null,
                                    fromDate.value,
                                    untilDate.value
                                )
                                else viewModel.fetchOrders(null, null, null)
                            }
                        },
                        message = error.error.localizedMessage ?: "Unknown error",
                        showButtonBack = false,
                        modifier = modifier
                    )
                }

                else -> {
                    if (orderPagingItems.itemCount > 0) {
                        LazyColumn(
                            modifier = modifier
                                .fillMaxWidth()
                        ) {
                            items(orderPagingItems.itemCount) { index ->
                                OrderHistoryCard(
                                    data = mapOrderDataItemToDataOrderHistoryCard(orderPagingItems[index]!!),
                                    context = context,
                                    onNavigateToDetail = { id -> onNavigateToDetail(id) },
                                    clipboardManager = clipboardManager,
                                    modifier = modifier
                                )
                            }
                        }
                    } else {
                        NotFoundScreen(modifier = modifier)
                    }
                }
            }

            if (showSheet.value) {
                BottomSheetOrderHistory(
                    fromDate = fromDate,
                    untilDate = untilDate,
                    isFilterOn = isFilterOn,
                    showSheet = showSheet,
                    modifier = modifier
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RiwayatScreenPreview() {
    JJSembakoTheme {
        RiwayatScreen(
            context = LocalContext.current,
            clipboardManager = LocalClipboardManager.current,
            onNavigateToDetail = {},
            onNavigateBack = {}
        )
    }
}