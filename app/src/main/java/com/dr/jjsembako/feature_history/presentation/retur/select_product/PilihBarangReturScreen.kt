package com.dr.jjsembako.feature_history.presentation.retur.select_product

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.components.bottom_sheet.BottomSheetProduct
import com.dr.jjsembako.core.presentation.components.utils.SearchFilter
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.rememberMutableStateListOf
import com.dr.jjsembako.core.utils.rememberMutableStateMapOf
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlin.math.roundToInt

@Composable
fun PilihBarangReturScreen(
    id: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    PilihBarangReturContent(id = id, onNavigateBack = { onNavigateBack() }, modifier = modifier)
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun PilihBarangReturContent(
    id: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val showSheet = remember { mutableStateOf(false) }
    val checkBoxResult = rememberMutableStateListOf<String>()
    val checkBoxStates = rememberMutableStateMapOf<String, Boolean>()
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val activeSearch = remember { mutableStateOf(false) }
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
            PullRefreshIndicator(
                refreshing = isRefreshing.value,
                state = pullRefreshState,
                modifier = modifier
                    .fillMaxWidth()
                    .height((pullRefreshState.progress * 100).roundToInt().dp)
            )

            SearchFilter(
                placeholder = stringResource(R.string.search_product),
                activeSearch,
                searchQuery,
                searchFunction = { },
                openFilter = { showSheet.value = !showSheet.value },
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))

            if (showSheet.value) {
                BottomSheetProduct(
                    optionList = null,
                    checkBoxResult = checkBoxResult,
                    checkBoxStates = checkBoxStates,
                    showSheet = showSheet,
                    modifier = modifier
                )
            }
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