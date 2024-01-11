package com.dr.jjsembako.feature_order.presentation.select_product

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
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.unit.dp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.components.BottomSheetProduct
import com.dr.jjsembako.core.presentation.components.HeaderError
import com.dr.jjsembako.core.presentation.components.LoadingScreen
import com.dr.jjsembako.core.presentation.components.NotFoundScreen
import com.dr.jjsembako.core.presentation.components.SearchFilter
import com.dr.jjsembako.core.utils.rememberMutableStateListOf
import com.dr.jjsembako.core.utils.rememberMutableStateMapOf
import com.dr.jjsembako.feature_order.presentation.components.ProductOnOrder

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProductListContent(pilihBarangViewModel: PilihBarangViewModel, modifier: Modifier) {
    val dataProducts = pilihBarangViewModel.dataProducts.observeAsState().value
    val option = pilihBarangViewModel.dataCategories.observeAsState().value
    val loadingState = pilihBarangViewModel.loadingState.observeAsState().value
    val errorState = pilihBarangViewModel.errorState.observeAsState().value
    val errorMsg = pilihBarangViewModel.errorMsg.observeAsState().value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val snackbarHostState = remember { SnackbarHostState() }
    val showSheet = remember { mutableStateOf(false) }
    val checkBoxResult = rememberMutableStateListOf<String>()
    val checkBoxStates = rememberMutableStateMapOf<String, Boolean>()
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val activeSearch = remember { mutableStateOf(false) }

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
                                ProductOnOrder(product = product, modifier = modifier)
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
    }
}