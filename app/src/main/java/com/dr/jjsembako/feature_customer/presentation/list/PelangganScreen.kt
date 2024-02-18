package com.dr.jjsembako.feature_customer.presentation.list

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
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
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.core.presentation.components.bottom_sheet.BottomSheetCustomer
import com.dr.jjsembako.core.presentation.components.card.CustomerInfo
import com.dr.jjsembako.core.presentation.components.screen.ErrorScreen
import com.dr.jjsembako.core.presentation.components.screen.LoadingScreen
import com.dr.jjsembako.core.presentation.components.screen.NotFoundScreen
import com.dr.jjsembako.core.presentation.components.utils.SearchFilter
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PelangganScreen(
    keyword: String,
    onNavigateBack: () -> Unit,
    onNavigateToDetailCust: (String, String) -> Unit,
    onNavigateToAddCust: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val pelangganViewModel: PelangganViewModel = hiltViewModel()
    val customerPagingItems: LazyPagingItems<DataCustomer> =
        pelangganViewModel.customerState.collectAsLazyPagingItems()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val showSheet = remember { mutableStateOf(false) }
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val activeSearch = remember { mutableStateOf(false) }

    // Set keyword for the first time Composable is rendered
    LaunchedEffect(keyword) {
        if (keyword.isEmpty()) {
            if (searchQuery.value.isEmpty()) pelangganViewModel.fetchCustomers()
            else pelangganViewModel.fetchCustomers(searchQuery.value)
        } else {
            if (searchQuery.value.isEmpty()) {
                searchQuery.value = keyword
                pelangganViewModel.fetchCustomers(keyword)
            } else pelangganViewModel.fetchCustomers(searchQuery.value)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = { Text(stringResource(R.string.cust)) },
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
        },
        floatingActionButton = {
            if (!activeSearch.value) {
                FloatingActionButton(
                    onClick = { onNavigateToAddCust(searchQuery.value) },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = stringResource(R.string.add_new_cust)
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
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
                placeholder = stringResource(R.string.search_cust),
                activeSearch,
                searchQuery,
                searchFunction = {
                    if (searchQuery.value.isEmpty()) pelangganViewModel.fetchCustomers()
                    else pelangganViewModel.fetchCustomers(searchQuery.value)

                },
                openFilter = { showSheet.value = !showSheet.value },
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))

            when (customerPagingItems.loadState.refresh) {
                LoadState.Loading -> {
                    LoadingScreen(modifier = modifier)
                }

                is LoadState.Error -> {
                    val error = customerPagingItems.loadState.refresh as LoadState.Error
                    Log.e("PelangganScreen", "Error")
                    ErrorScreen(
                        onNavigateBack = { },
                        onReload = {
                            if (searchQuery.value.isEmpty()) pelangganViewModel.fetchCustomers()
                            else pelangganViewModel.fetchCustomers(searchQuery.value)
                        },
                        message = error.error.localizedMessage ?: "Unknown error",
                        showButtonBack = false,
                        modifier = modifier
                    )
                }

                else -> {
                    if (customerPagingItems.itemCount > 0) {
                        LazyColumn(
                            modifier = modifier
                                .fillMaxWidth()
                        ) {
                            items(customerPagingItems.itemCount) { index ->
                                CustomerInfo(
                                    onNavigateToDetailCust = {
                                        onNavigateToDetailCust(
                                            customerPagingItems[index]!!.id,
                                            searchQuery.value
                                        )
                                    },
                                    customer = customerPagingItems[index]!!,
                                    modifier = modifier
                                )
                                Spacer(modifier = modifier.height(8.dp))
                            }
                        }
                    } else {
                        NotFoundScreen(modifier = modifier)
                    }
                }
            }

            if (showSheet.value) {
                BottomSheetCustomer(
                    optionList = radioOptions,
                    selectedOption = selectedOption,
                    onOptionSelected = onOptionSelected,
                    showSheet = showSheet,
                    modifier = modifier
                )
            }
        }
    }
}

private val radioOptions = listOf(
    FilterOption("Semua Pelanggan", "semua"),
    FilterOption("Pelanggan Saya", "pelangganku")
)

@Composable
@Preview(showBackground = true)
private fun PelangganScreenPreview() {
    JJSembakoTheme {
        PelangganScreen(
            keyword = "",
            onNavigateBack = {},
            onNavigateToDetailCust = { id, keyword ->
                Log.d(
                    "DEBUG",
                    "id: $id with keyword: $keyword"
                )
            },
            onNavigateToAddCust = {},
            modifier = Modifier
        )
    }
}