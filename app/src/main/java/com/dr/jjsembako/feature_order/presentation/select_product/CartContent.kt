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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dr.jjsembako.core.presentation.components.HeaderError
import com.dr.jjsembako.core.presentation.components.LoadingScreen
import com.dr.jjsembako.core.presentation.components.NotFoundScreen
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_order.presentation.components.ProductOnOrder

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CartContent(pilihBarangViewModel: PilihBarangViewModel, modifier: Modifier) {
    val dataProducts = pilihBarangViewModel.dataProducts.observeAsState().value
    val loadingState = pilihBarangViewModel.loadingState.observeAsState().value
    val errorState = pilihBarangViewModel.errorState.observeAsState().value
    val errorMsg = pilihBarangViewModel.errorMsg.observeAsState().value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorState) {
        if (errorState == true && !errorMsg.isNullOrEmpty()) {
            snackbarHostState.showSnackbar(message = errorMsg)
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
                    focusManager.clearFocus()
                })
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (errorState == true && !errorMsg.isNullOrEmpty()) {
            HeaderError(modifier = modifier, message = errorMsg)
            Spacer(modifier = modifier.height(16.dp))
        }
        Spacer(modifier = modifier.height(16.dp))

        if (loadingState == true) {
            LoadingScreen(modifier = modifier)
        } else {
            if (dataProducts.isNullOrEmpty()) {
                NotFoundScreen(modifier = modifier)
            } else {
                val filteredProducts = dataProducts.filter { product ->
                    product!!.isChosen
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
                                ProductOnOrder(pilihBarangViewModel, product, modifier)
                            }
                            Spacer(modifier = modifier.height(8.dp))
                        })
                    }
                } else {
                    NotFoundScreen(modifier = modifier)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CartContentPreview() {
    JJSembakoTheme {
        val pilihBarangViewModel: PilihBarangViewModel = hiltViewModel()
        CartContent(
            pilihBarangViewModel = pilihBarangViewModel,
            modifier = Modifier
        )
    }
}