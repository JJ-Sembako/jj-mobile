package com.dr.jjsembako.feature_history.presentation.edit_product_order

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.dummy.detailOrder
import com.dr.jjsembako.core.data.remote.response.order.DetailOrder
import com.dr.jjsembako.core.presentation.components.screen.LoadingScreen
import com.dr.jjsembako.core.presentation.components.screen.NotFoundScreen
import com.dr.jjsembako.core.presentation.components.utils.HeaderError
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_history.presentation.components.ChangeTotalPayment
import com.dr.jjsembako.feature_history.presentation.components.card.UpdateOrderCard

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CartContentEdit(
    orderData: DetailOrder,
    viewModel: EditBarangPesananViewModel,
    showDialog: MutableState<Boolean>,
    previewProductName: MutableState<String>,
    previewProductImage: MutableState<String>,
    modifier: Modifier
) {
    val tag = "Cart Content"
    val dataProducts = viewModel.dataProducts.observeAsState().value
    val loadingState = viewModel.loadingState.observeAsState().value
    val errorState = viewModel.errorState.observeAsState().value
    val errorMsg = viewModel.errorMsg.observeAsState().value
    val selectedData = viewModel.selectedData.observeAsState().value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val changeCost = rememberSaveable { mutableLongStateOf(0L) }
    val changeQty = rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(
        Unit,
        selectedData?.orderQty,
        selectedData?.orderPrice,
        selectedData?.orderTotalPrice
    ) {
        if (selectedData != null) {
            val orderInfo = orderData.orderToProducts.find { it.product.id == selectedData.id }
            if (orderInfo != null) {
                changeQty.intValue = selectedData.orderQty - orderInfo.actualAmount
                changeCost.longValue =
                    selectedData.orderTotalPrice - (orderInfo.actualAmount * orderInfo.selledPrice)
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
                    focusManager.clearFocus()
                })
            .padding(vertical = 16.dp),
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
                val filteredProducts = dataProducts.filter { product -> product!!.isChosen }

                if (filteredProducts.isNotEmpty()) {
                    LazyColumn(modifier = modifier.fillMaxWidth()) {
                        item {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.DeleteSweep,
                                    contentDescription = stringResource(R.string.clear_data),
                                    tint = Color.Red,
                                    modifier = modifier
                                        .size(32.dp)
                                        .clickable { viewModel.reset() }
                                )
                            }
                            Spacer(modifier = modifier.height(16.dp))
                        }
                        item {
                            Column(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                filteredProducts.first().let { product ->
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
                                }
                            }
                        }
                        item {
                            Spacer(modifier = modifier.height(16.dp))
                            ChangeTotalPayment(
                                orderCost = orderData.actualTotalPrice,
                                changeCost = changeCost.longValue,
                                modifier = modifier,
                                isForUpdate = true,
                                changeQty = changeQty.intValue
                            )
                        }
                    }

                } else {
                    NotFoundScreen(modifier = modifier)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CartContentEditPreview() {
    JJSembakoTheme {
        CartContentEdit(
            orderData = detailOrder,
            viewModel = hiltViewModel(),
            showDialog = remember { mutableStateOf(true) },
            previewProductName = remember { mutableStateOf("") },
            previewProductImage = remember { mutableStateOf("") },
            modifier = Modifier
        )
    }
}