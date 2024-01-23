package com.dr.jjsembako.feature_order.presentation.select_product

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.components.utils.HeaderError
import com.dr.jjsembako.core.presentation.components.screen.LoadingScreen
import com.dr.jjsembako.core.presentation.components.screen.NotFoundScreen
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_order.presentation.components.ProductOnOrder

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CartContent(pilihBarangViewModel: PilihBarangViewModel, modifier: Modifier) {
    val tag = "Cart Content"
    val dataProducts = pilihBarangViewModel.dataProducts.observeAsState().value
    val loadingState = pilihBarangViewModel.loadingState.observeAsState().value
    val errorState = pilihBarangViewModel.errorState.observeAsState().value
    val errorMsg = pilihBarangViewModel.errorMsg.observeAsState().value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

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
            Log.e(tag, errorMsg)
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
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.selected_num, filteredProducts.size),
                            fontSize = 16.sp, fontWeight = FontWeight.Light,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )
                        Spacer(modifier = modifier.width(16.dp))
                        Icon(
                            Icons.Default.DeleteSweep,
                            contentDescription = stringResource(R.string.clear_data),
                            tint = Color.Red,
                            modifier = modifier
                                .size(32.dp)
                                .clickable { pilihBarangViewModel.reset() }
                        )
                    }

                    Spacer(modifier = modifier.height(16.dp))

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