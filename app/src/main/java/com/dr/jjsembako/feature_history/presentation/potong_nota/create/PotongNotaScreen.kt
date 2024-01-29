package com.dr.jjsembako.feature_history.presentation.potong_nota.create

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_history.presentation.components.potong_nota.PNHeader
import com.dr.jjsembako.feature_history.presentation.components.potong_nota.PNSelectedProduct
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@Composable
fun PotongNotaScreen(
    context: Context,
    clipboardManager: ClipboardManager,
    onNavigateBack: () -> Unit,
    onSelectProduct: () -> Unit,
    modifier: Modifier = Modifier
) {
    PotongNotaContent(
        context = context,
        clipboardManager = clipboardManager,
        onNavigateBack = { onNavigateBack() },
        onSelectProduct = { onSelectProduct() },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun PotongNotaContent(
    context: Context,
    clipboardManager: ClipboardManager,
    onNavigateBack: () -> Unit,
    onSelectProduct: () -> Unit,
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
                title = { Text(stringResource(R.string.potong_nota)) },
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
            PNHeader(context = context, clipboardManager = clipboardManager, modifier = modifier)
            Spacer(modifier = modifier.height(16.dp))
            PNSelectedProduct(onSelectProduct = { onSelectProduct() }, modifier = modifier)
            Spacer(modifier = modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PotongNotaScreenPreview() {
    JJSembakoTheme {
        PotongNotaScreen(
            context = LocalContext.current,
            clipboardManager = LocalClipboardManager.current,
            onNavigateBack = {},
            onSelectProduct = {}
        )
    }
}