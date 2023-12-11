package com.dr.jjsembako.feature_customer.presentation.list

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dr.jjsembako.R
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.presentation.components.BottomSheetCustomer
import com.dr.jjsembako.core.presentation.components.CustomerInfo
import com.dr.jjsembako.core.presentation.components.ErrorScreen
import com.dr.jjsembako.core.presentation.components.LoadingScreen
import com.dr.jjsembako.core.presentation.components.SearchFilter
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.navigation.Screen

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PelangganScreen(
    navController: NavHostController,
    onNavigateBack: () -> Unit,
    onNavigateToDetailCust: (String) -> Unit,
    onNavigateToAddCust: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pelangganViewModel: PelangganViewModel = hiltViewModel()
    val state = pelangganViewModel.state.observeAsState().value
    val statusCode = pelangganViewModel.statusCode.observeAsState().value
    val message = pelangganViewModel.message.observeAsState().value
    val customerList = pelangganViewModel.customerList.observeAsState().value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showSheet = remember { mutableStateOf(false) }
    var (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    var searchQuery = remember { mutableStateOf("") }
    var activeSearch = remember { mutableStateOf(false) }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_empty))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )

    LaunchedEffect(Unit) {
        if (navController.currentBackStackEntry?.destination?.route == Screen.DetailPelanggan.route) {
            pelangganViewModel.fetchCustomers(search = searchQuery.value)
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
                    onClick = { onNavigateToAddCust() },
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
                    else pelangganViewModel.fetchCustomers(search = searchQuery.value)

                },
                openFilter = { showSheet.value = !showSheet.value },
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))

            when (state) {
                StateResponse.LOADING -> {
                    LoadingScreen(modifier = modifier)
                }

                StateResponse.ERROR -> {
                    Log.e("PelangganScreen", "Error")
                    Log.e("PelangganScreen", "state: $state")
                    Log.e("PelangganScreen", "Error: $message")
                    Log.e("PelangganScreen", "statusCode: $statusCode")
                    ErrorScreen(
                        onNavigateBack = { },
                        onReload = {
                            if (searchQuery.value.isEmpty()) pelangganViewModel.fetchCustomers()
                            else pelangganViewModel.fetchCustomers(search = searchQuery.value)
                        },
                        message = message ?: "Unknown error",
                        showButtonBack = false,
                        modifier = modifier
                    )
                }

                StateResponse.SUCCESS -> {
                    if (customerList != null) {
                        if (customerList.isNotEmpty()) {
                            LazyColumn(
                                modifier = modifier
                                    .fillMaxWidth()
                            ) {
                                items(items = customerList, itemContent = { cust ->
                                    if (cust != null) {
                                        CustomerInfo(
                                            onNavigateToDetailCust = { onNavigateToDetailCust(cust.id) },
                                            customer = cust,
                                            modifier = modifier
                                        )
                                        Spacer(modifier = modifier.height(8.dp))
                                    }
                                })
                            }
                        } else {
                            Spacer(modifier = modifier.height(48.dp))
                            Column(
                                modifier = modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                LottieAnimation(
                                    enableMergePaths = true,
                                    composition = composition,
                                    progress = { progress },
                                    modifier = modifier.size(150.dp)
                                )
                                Spacer(modifier = modifier.height(16.dp))
                                Text(
                                    text = stringResource(R.string.not_found),
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = modifier.wrapContentSize(Alignment.Center)
                                )
                            }
                            Spacer(modifier = modifier.height(16.dp))
                        }
                    }
                }

                else -> {}
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
fun PelangganScreenPreview() {
    JJSembakoTheme {
        PelangganScreen(
            navController = rememberNavController(),
            onNavigateBack = {},
            onNavigateToDetailCust = {},
            onNavigateToAddCust = {},
            modifier = Modifier
        )
    }
}