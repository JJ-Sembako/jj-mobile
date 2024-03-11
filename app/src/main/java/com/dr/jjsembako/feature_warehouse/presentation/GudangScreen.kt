package com.dr.jjsembako.feature_warehouse.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.components.bottom_sheet.BottomSheetProduct
import com.dr.jjsembako.core.presentation.components.dialog.PreviewImageDialog
import com.dr.jjsembako.core.presentation.components.screen.LoadingScreen
import com.dr.jjsembako.core.presentation.components.screen.NotFoundScreen
import com.dr.jjsembako.core.presentation.components.utils.HeaderError
import com.dr.jjsembako.core.presentation.components.utils.SearchFilter
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.rememberMutableStateListOf
import com.dr.jjsembako.core.utils.rememberMutableStateMapOf
import com.dr.jjsembako.feature_warehouse.presentation.components.ProductOnWarehouseInfo

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GudangScreen(onNavigateBack: () -> Unit, modifier: Modifier = Modifier) {
    val gudangViewModel: GudangViewModel = hiltViewModel()
    val dataProducts = gudangViewModel.dataProducts.observeAsState().value
    val option = gudangViewModel.dataCategories.observeAsState().value
    val loadingState = gudangViewModel.loadingState.observeAsState().value
    val errorState = gudangViewModel.errorState.observeAsState().value
    val errorMsg = gudangViewModel.errorMsg.observeAsState().value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val snackbarHostState = remember { SnackbarHostState() }
    val showSheet = remember { mutableStateOf(false) }
    val checkBoxResult = rememberMutableStateListOf<String>()
    val checkBoxStates = rememberMutableStateMapOf<String, Boolean>()
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val activeSearch = remember { mutableStateOf(false) }
    val showPreviewImageDialog = remember { mutableStateOf(false) }
    val previewProductName = remember { mutableStateOf("") }
    val previewProductImage = remember { mutableStateOf("") }

    LaunchedEffect(errorState) {
        if (errorState == true && !errorMsg.isNullOrEmpty()) {
            snackbarHostState.showSnackbar(message = errorMsg)
        }
    }

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

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = { Text(stringResource(R.string.warehouse)) },
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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
            if (errorState == true && !errorMsg.isNullOrEmpty()) {
                HeaderError(modifier = modifier, message = errorMsg)
                Spacer(modifier = modifier.height(16.dp))
            }
            SearchFilter(
                placeholder = stringResource(R.string.search_product),
                activeSearch,
                searchQuery,
                searchFunction = { },
                openFilter = { showSheet.value = !showSheet.value },
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))

            if (loadingState == true) {
                LoadingScreen(modifier = modifier)
            } else {
                if (dataProducts.isNullOrEmpty()) {
                    NotFoundScreen(modifier = modifier)
                } else {
                    val filteredProducts = dataProducts.filter { product ->
                        product!!.name.contains(searchQuery.value, ignoreCase = true) &&
                                checkBoxResult.isNotEmpty()
                                && checkBoxResult.contains(product.category)
                    }

                    if (filteredProducts.isNotEmpty()) {
                        LazyColumn(
                            modifier = modifier
                                .fillMaxWidth()
                        ) {
                            items(items = filteredProducts, key = { product ->
                                product?.id ?: "empty-${System.currentTimeMillis()}"
                            }, itemContent = { product ->
                                if (product != null) {
                                    ProductOnWarehouseInfo(
                                        product = product,
                                        showDialog = showPreviewImageDialog,
                                        previewProductName = previewProductName,
                                        previewProductImage = previewProductImage,
                                        modifier = modifier
                                    )
                                }
                                Spacer(modifier = modifier.height(8.dp))
                            })
                        }
                    } else {
                        NotFoundScreen(modifier = modifier)
                    }
                }
            }

            if (showSheet.value) {
                BottomSheetProduct(
                    optionList = option,
                    checkBoxResult = checkBoxResult,
                    checkBoxStates = checkBoxStates,
                    showSheet = showSheet,
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
    }
}

@Preview(showBackground = true)
@Composable
private fun GudangScreenPreview() {
    JJSembakoTheme {
        GudangScreen(
            onNavigateBack = {}
        )
    }
}