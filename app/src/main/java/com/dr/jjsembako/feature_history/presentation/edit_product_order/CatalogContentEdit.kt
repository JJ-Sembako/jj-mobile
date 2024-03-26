package com.dr.jjsembako.feature_history.presentation.edit_product_order

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
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.dr.jjsembako.core.data.dummy.dataDetailOrder
import com.dr.jjsembako.core.data.remote.response.order.DataDetailOrder
import com.dr.jjsembako.core.presentation.components.bottom_sheet.BottomSheetProduct
import com.dr.jjsembako.core.presentation.components.screen.LoadingScreen
import com.dr.jjsembako.core.presentation.components.screen.NotFoundScreen
import com.dr.jjsembako.core.presentation.components.utils.HeaderError
import com.dr.jjsembako.core.presentation.components.utils.SearchFilter
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.rememberMutableStateListOf
import com.dr.jjsembako.core.utils.rememberMutableStateMapOf
import com.dr.jjsembako.feature_history.presentation.components.card.UpdateOrderCard

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CatalogContentEdit(
    orderData: DataDetailOrder,
    viewModel: EditBarangPesananViewModel,
    showDialog: MutableState<Boolean>,
    previewProductName: MutableState<String>,
    previewProductImage: MutableState<String>,
    modifier: Modifier
) {
    val tag = "Catalog Content"
    val dataProducts = viewModel.dataProducts.observeAsState().value
    val option = viewModel.dataCategories.observeAsState().value
    val loadingState = viewModel.loadingState.observeAsState().value
    val errorState = viewModel.errorState.observeAsState().value
    val errorMsg = viewModel.errorMsg.observeAsState().value

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
            Log.e(tag, errorMsg)
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
                            && orderData.orderToProducts.any { it.product.id == product.id }
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
                                val orderInfo =
                                    orderData.orderToProducts.find { it.product.id == product.id }
                                if (orderInfo != null) {
                                    UpdateOrderCard(
                                        viewModel, orderInfo, product, showDialog,
                                        previewProductName, previewProductImage, modifier
                                    )
                                }
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

@Preview(showBackground = true)
@Composable
private fun CatalogContentEditPreview() {
    JJSembakoTheme {
        CatalogContentEdit(
            orderData = dataDetailOrder,
            viewModel = hiltViewModel(),
            showDialog = remember { mutableStateOf(true) },
            previewProductName = remember { mutableStateOf("") },
            previewProductImage = remember { mutableStateOf("") },
            modifier = Modifier
        )
    }
}