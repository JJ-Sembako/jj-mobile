package com.dr.jjsembako.feature_order.presentation.select_cust

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dr.jjsembako.R
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.core.presentation.components.AlertErrorDialog
import com.dr.jjsembako.core.presentation.components.BottomSheetCustomer
import com.dr.jjsembako.core.presentation.components.CardEmpty
import com.dr.jjsembako.core.presentation.components.ErrorScreen
import com.dr.jjsembako.core.presentation.components.LoadingDialog
import com.dr.jjsembako.core.presentation.components.LoadingScreen
import com.dr.jjsembako.core.presentation.components.NotFoundScreen
import com.dr.jjsembako.core.presentation.components.SearchFilter
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_order.presentation.components.CustomerInfoOrder

@Composable
fun PilihPelangganScreen(
    onNavigateToMainOrderScreen: () -> Unit,
    modifier: Modifier = Modifier,
    idSelectedCustomer: String = ""
) {
    val tag = "Pilih Pelanggan Screen"
    val pilihPelangganViewModel: PilihPelangganViewModel = hiltViewModel()
    val state = pilihPelangganViewModel.state.observeAsState().value
    val message = pilihPelangganViewModel.message.observeAsState().value
    val statusCode = pilihPelangganViewModel.statusCode.observeAsState().value

    if (idSelectedCustomer.isEmpty()) {
        PilihPelangganContent(
            pilihPelangganViewModel = pilihPelangganViewModel,
            onNavigateToMainOrderScreen = { onNavigateToMainOrderScreen() },
            modifier = modifier
        )
    } else {
        LaunchedEffect(idSelectedCustomer) {
            if (idSelectedCustomer.isNotEmpty()) {
                pilihPelangganViewModel.fetchDetailCustomer(idSelectedCustomer)
            }
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
                    onNavigateBack = { onNavigateToMainOrderScreen() },
                    onReload = {
                        pilihPelangganViewModel.fetchDetailCustomer(idSelectedCustomer)
                    },
                    message = message ?: "Unknown error",
                    modifier = modifier
                )
            }

            StateResponse.SUCCESS -> {
                PilihPelangganContent(
                    pilihPelangganViewModel = pilihPelangganViewModel,
                    onNavigateToMainOrderScreen = { onNavigateToMainOrderScreen() },
                    modifier = modifier
                )
            }

            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun PilihPelangganContent(
    pilihPelangganViewModel: PilihPelangganViewModel,
    onNavigateToMainOrderScreen: () -> Unit,
    modifier: Modifier
) {
    val tag = "Pilih Pelanggan Content"
    val stateRefresh = pilihPelangganViewModel.stateRefresh.observeAsState().value
    val message = pilihPelangganViewModel.message.observeAsState().value
    val statusCode = pilihPelangganViewModel.statusCode.observeAsState().value
    val selectedCustomer by pilihPelangganViewModel.selectedCustomer.observeAsState()
    val customerPagingItems: LazyPagingItems<DataCustomer> =
        pilihPelangganViewModel.customerState.collectAsLazyPagingItems()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val showSheet = remember { mutableStateOf(false) }
    val showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    val showErrorDialog = rememberSaveable { mutableStateOf(false) }
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val activeSearch = remember { mutableStateOf(false) }

    // Set keyword for the first time Composable is rendered
    LaunchedEffect(Unit) {
        if (searchQuery.value.isEmpty()) pilihPelangganViewModel.fetchCustomers()
        else pilihPelangganViewModel.fetchCustomers(searchQuery.value)
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
            pilihPelangganViewModel.setStateRefresh(null)
        }

        StateResponse.SUCCESS -> {
            showLoadingDialog.value = false
            showErrorDialog.value = false
            pilihPelangganViewModel.setStateRefresh(null)
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
                title = { Text(stringResource(R.string.select_cust)) },
                navigationIcon = {
                    IconButton(onClick = {
                        keyboardController?.hide()
                        onNavigateToMainOrderScreen()
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
                        keyboardController?.hide()
                        onNavigateToMainOrderScreen()
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
            Text(
                text = stringResource(R.string.selected_data),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            if (!selectedCustomer?.id.isNullOrEmpty()) {
                val data = DataCustomer(
                    id = selectedCustomer!!.id,
                    name = selectedCustomer!!.name,
                    shopName = selectedCustomer!!.shopName,
                    address = selectedCustomer!!.address,
                    gmapsLink = selectedCustomer!!.gmapsLink,
                    phoneNumber = selectedCustomer!!.phoneNumber,
                    debt = selectedCustomer!!.debt
                )
                Spacer(modifier = modifier.height(8.dp))
                CustomerInfoOrder(
                    pilihPelangganViewModel = pilihPelangganViewModel,
                    selectedCust = selectedCustomer,
                    customer = data,
                    modifier = modifier
                )
                Spacer(modifier = modifier.height(16.dp))
            } else {
                Spacer(modifier = modifier.height(8.dp))
                CardEmpty(modifier)
                Spacer(modifier = modifier.height(16.dp))
            }

            SearchFilter(
                placeholder = stringResource(R.string.search_cust),
                activeSearch,
                searchQuery,
                searchFunction = {
                    if (searchQuery.value.isEmpty()) pilihPelangganViewModel.fetchCustomers()
                    else pilihPelangganViewModel.fetchCustomers(searchQuery.value)

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
                            if (searchQuery.value.isEmpty()) pilihPelangganViewModel.fetchCustomers()
                            else pilihPelangganViewModel.fetchCustomers(searchQuery.value)
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
                                CustomerInfoOrder(
                                    pilihPelangganViewModel = pilihPelangganViewModel,
                                    selectedCust = selectedCustomer,
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

            if (showLoadingDialog.value) {
                LoadingDialog(showLoadingDialog, modifier)
            }

            if (showErrorDialog.value) {
                AlertErrorDialog(
                    message = message ?: stringResource(id = R.string.error_msg_default),
                    showDialog = showErrorDialog,
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
private fun PilihPelangganScreenPreview() {
    JJSembakoTheme {
        PilihPelangganScreen(
            onNavigateToMainOrderScreen = {},
            modifier = Modifier
        )
    }
}