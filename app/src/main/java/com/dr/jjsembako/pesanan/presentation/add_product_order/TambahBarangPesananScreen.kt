package com.dr.jjsembako.pesanan.presentation.add_product_order

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.dr.jjsembako.R
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.pesanan.domain.model.DetailOrder
import com.dr.jjsembako.core.presentation.components.dialog.AlertErrorDialog
import com.dr.jjsembako.core.presentation.components.dialog.LoadingDialog
import com.dr.jjsembako.core.presentation.components.dialog.PreviewImageDialog
import com.dr.jjsembako.core.presentation.components.screen.ErrorScreen
import com.dr.jjsembako.core.presentation.components.screen.LoadingScreen
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@Composable
fun TambahBarangPesananScreen(
    id: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tag = "TambahBarangPesanan-S"
    val viewModel: TambahBarangPesananViewModel = hiltViewModel()
    val stateFirst = viewModel.stateFirst.observeAsState().value
    val statusCode = viewModel.statusCode.observeAsState().value
    val message = viewModel.message.observeAsState().value
    val orderData = viewModel.orderData

    // Set id for the first time Composable is rendered
    LaunchedEffect(id) {
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
                TambahBarangPesananContent(
                    orderData = orderData,
                    viewModel = viewModel,
                    onNavigateBack = onNavigateBack,
                    modifier = modifier
                )
            }
        }

        else -> {}
    }

}

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
private fun TambahBarangPesananContent(
    orderData: DetailOrder,
    viewModel: TambahBarangPesananViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tag = "TambahBarangPesanan-C"
    val stateSecond = viewModel.stateSecond.observeAsState().value
    val stateRefresh = viewModel.stateRefresh.observeAsState().value
    val isRefreshing by viewModel.isRefreshing.collectAsState(initial = false)
    val statusCode = viewModel.statusCode.observeAsState().value
    val message = viewModel.message.observeAsState().value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf(stringResource(R.string.cart), stringResource(R.string.catalog))
    val pagerState = rememberPagerState { tabs.size }

    val showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    val showErrorDialog = remember { mutableStateOf(false) }
    val showPreviewImageDialog = remember { mutableStateOf(false) }
    val previewProductName = remember { mutableStateOf("") }
    val previewProductImage = remember { mutableStateOf("") }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refresh() })

    LaunchedEffect(tabIndex) {
        pagerState.animateScrollToPage(tabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            tabIndex = pagerState.currentPage
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
                title = { Text(stringResource(R.string.add_product_order)) },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.handleAddProductOrder()
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
                            focusManager.clearFocus()
                        }),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TabRow(selectedTabIndex = tabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(text = { Text(title) },
                            selected = tabIndex == index,
                            onClick = { tabIndex = index }
                        )
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    modifier = modifier
                        .fillMaxWidth()
                ) { index ->
                    when (index) {
                        0 -> CartContentAdd(
                            orderData, viewModel, showPreviewImageDialog,
                            previewProductName, previewProductImage, modifier
                        )

                        1 -> CatalogContentAdd(
                            orderData, viewModel, showPreviewImageDialog,
                            previewProductName, previewProductImage, modifier
                        )

                        else -> {}
                    }
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
private fun TambahBarangPesananScreenPreview() {
    JJSembakoTheme {
        TambahBarangPesananScreen(
            id = "123",
            onNavigateBack = {},
        )
    }
}