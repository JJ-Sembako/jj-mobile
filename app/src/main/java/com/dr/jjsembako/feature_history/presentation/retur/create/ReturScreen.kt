package com.dr.jjsembako.feature_history.presentation.retur.create

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.dummy.dataOrderDataItem
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_history.presentation.components.PNRHeader
import com.dr.jjsembako.feature_history.presentation.components.PNRTotalPayment
import com.dr.jjsembako.feature_history.presentation.components.retur.RSelectedProduct
import com.dr.jjsembako.feature_history.presentation.components.retur.RSelectedSubstitute
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlin.math.roundToInt

@Composable
fun ReturScreen(
    id: String,
    context: Context,
    clipboardManager: ClipboardManager,
    onNavigateBack: () -> Unit,
    onSelectProduct: () -> Unit,
    onSelectSubstitute: () -> Unit,
    modifier: Modifier = Modifier
) {
    ReturContent(
        id = id,
        context = context,
        clipboardManager = clipboardManager,
        onNavigateBack = { onNavigateBack() },
        onSelectProduct = { onSelectProduct() },
        onSelectSubstitute = { onSelectSubstitute() },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReturContent(
    id: String,
    context: Context,
    clipboardManager: ClipboardManager,
    onNavigateBack: () -> Unit,
    onSelectProduct: () -> Unit,
    onSelectSubstitute: () -> Unit,
    modifier: Modifier
) {
    val scrollState = rememberScrollState()
    val showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    val showErrorDialog = rememberSaveable { mutableStateOf(false) }
    val isRefreshing = remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing.value,
        onRefresh = {})

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = { Text(stringResource(R.string.retur)) },
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
                        onNavigateBack()
                        //TODO: Call function
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
                .verticalScroll(scrollState)
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PullRefreshIndicator(
                refreshing = isRefreshing.value,
                state = pullRefreshState,
                modifier = modifier
                    .fillMaxWidth()
                    .height((pullRefreshState.progress * 100).roundToInt().dp)
            )

            PNRHeader(
                data = dataOrderDataItem,
                context = context,
                clipboardManager = clipboardManager,
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))
            RSelectedProduct(onSelectProduct = { onSelectProduct() }, modifier = modifier)
            Spacer(modifier = modifier.height(16.dp))
            RSelectedSubstitute(onSelectSubstitute = { onSelectSubstitute() }, modifier = modifier)
            Spacer(modifier = modifier.height(16.dp))
            PNRTotalPayment(orderCost = 1_500_000L, changeCost = 125_000L, modifier = modifier)
            Spacer(modifier = modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReturScreenPreview() {
    JJSembakoTheme {
        ReturScreen(
            id = "123",
            context = LocalContext.current,
            clipboardManager = LocalClipboardManager.current,
            onNavigateBack = {},
            onSelectProduct = {},
            onSelectSubstitute = {}
        )
    }
}