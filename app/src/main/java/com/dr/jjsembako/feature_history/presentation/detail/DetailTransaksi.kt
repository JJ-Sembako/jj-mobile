package com.dr.jjsembako.feature_history.presentation.detail

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.components.dialog.OrderInformationDialog
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_history.presentation.components.detail.CustomerInformation
import com.dr.jjsembako.feature_history.presentation.components.detail.OrderButtonMenu
import com.dr.jjsembako.feature_history.presentation.components.detail.OrderInformation
import com.dr.jjsembako.feature_history.presentation.components.detail.OrderTimestamps
import com.dr.jjsembako.feature_history.presentation.components.detail.OrderedProductList
import com.dr.jjsembako.feature_history.presentation.components.detail.ReturPotongNotaInformation
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlin.math.roundToInt

@Composable
fun DetailTransaksi(
    id: String,
    context: Context,
    clipboardManager: ClipboardManager,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    DetailTransaksiContent(
        id = "",
        context = context,
        clipboardManager = clipboardManager,
        onNavigateBack = { onNavigateBack() },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailTransaksiContent(
    id: String,
    context: Context,
    clipboardManager: ClipboardManager,
    onNavigateBack: () -> Unit,
    modifier: Modifier
) {
    val scrollState = rememberScrollState()
    val showInfoDialog = remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
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
                title = { Text(stringResource(R.string.det_order)) },
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
                    IconButton(onClick = { showInfoDialog.value = !showInfoDialog.value }) {
                        Icon(
                            Icons.Default.Info,
                            stringResource(R.string.info),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(onClick = { menuExpanded = !menuExpanded }) {
                        Icon(
                            Icons.Default.MoreVert,
                            stringResource(R.string.menu),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    DropdownMenu(
                        modifier = modifier.widthIn(min = 200.dp),
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.edit)) },
                            onClick = { },
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.cancel_order)) },
                            onClick = { showDialog.value = true })
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

            OrderInformation(
                context = context,
                clipboardManager = clipboardManager,
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))
            OrderButtonMenu(modifier)
            Spacer(modifier = modifier.height(16.dp))
            CustomerInformation(modifier)
            Spacer(modifier = modifier.height(16.dp))
            OrderedProductList(modifier)
            Spacer(modifier = modifier.height(16.dp))
            OrderTimestamps(modifier)
            Spacer(modifier = modifier.height(64.dp))
            ReturPotongNotaInformation(modifier)
            Spacer(modifier = modifier.height(16.dp))

            if (showInfoDialog.value) {
                OrderInformationDialog(showDialog = showInfoDialog, modifier = modifier)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun DetailTransaksiPreview() {
    JJSembakoTheme {
        DetailTransaksi(
            id = "",
            context = LocalContext.current,
            clipboardManager = LocalClipboardManager.current,
            onNavigateBack = {},
            modifier = Modifier
        )
    }
}