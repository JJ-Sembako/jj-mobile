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
import com.dr.jjsembako.core.presentation.components.BottomSheetProduct
import com.dr.jjsembako.core.presentation.components.NotFoundScreen
import com.dr.jjsembako.core.presentation.components.SearchFilter
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

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val showSheet = remember { mutableStateOf(false) }
    val checkBoxResult = rememberMutableStateListOf<String>()
    val checkBoxStates = rememberMutableStateMapOf<String, Boolean>()
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val activeSearch = remember { mutableStateOf(false) }

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
                option.map { it!!.value }.filterNot { checkBoxStates.containsKey(it) }
                    .forEach { checkBoxStates[it] = false }
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
                placeholder = stringResource(R.string.search_product),
                activeSearch,
                searchQuery,
                searchFunction = { },
                openFilter = { showSheet.value = !showSheet.value },
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))

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
                                ProductOnWarehouseInfo(product = product, modifier = modifier)
                            }
                            Spacer(modifier = modifier.height(8.dp))
                        })
                    }
                } else {
                    NotFoundScreen(modifier = modifier)
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