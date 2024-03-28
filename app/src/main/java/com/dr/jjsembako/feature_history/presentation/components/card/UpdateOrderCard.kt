package com.dr.jjsembako.feature_history.presentation.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.dummy.dataOrderedProducts
import com.dr.jjsembako.core.data.dummy.dataProductOrder
import com.dr.jjsembako.core.data.model.DataProductOrder
import com.dr.jjsembako.core.data.remote.response.order.OrderedProduct
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.formatRupiah
import com.dr.jjsembako.core.utils.rememberCurrencyVisualTransformation
import com.dr.jjsembako.feature_history.presentation.edit_product_order.EditBarangPesananViewModel

@Composable
fun UpdateOrderCard(
    viewModel: EditBarangPesananViewModel,
    data: OrderedProduct,
    product: DataProductOrder,
    showDialog: MutableState<Boolean>,
    previewProductName: MutableState<String>,
    previewProductImage: MutableState<String>,
    modifier: Modifier
) {
    val changeAmount = rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(product.orderQty) {
        changeAmount.intValue = product.orderQty - data.amount
    }

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp),
        border = if (product.isChosen && (product.orderTotalPrice == 0L || changeAmount.intValue > product.stockInUnit)) {
            BorderStroke(3.dp, Color.Red)
        } else CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProductImage(product, showDialog, previewProductName, previewProductImage, modifier)
            OrderedProductInfo(data, modifier)
        }
        Divider(
            modifier = modifier
                .fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary
        )
        ProductInfo(product, modifier)
        Divider(
            modifier = modifier
                .fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary
        )
        OrderContent(viewModel, product, changeAmount, modifier)
    }
}

@Composable
private fun ProductImage(
    product: DataProductOrder,
    showDialog: MutableState<Boolean>,
    previewProductName: MutableState<String>,
    previewProductImage: MutableState<String>,
    modifier: Modifier
) {
    if (product.image.isEmpty() || product.image.contains("default")) {
        Image(
            painter = painterResource(id = R.drawable.ic_default),
            contentDescription = stringResource(R.string.product_description, product.name),
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clickable {
                    previewProductName.value = product.name
                    previewProductImage.value = product.image
                    showDialog.value = true
                }
                .padding(8.dp)
                .width(60.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    } else {
        AsyncImage(
            model = product.image,
            contentDescription = stringResource(R.string.product_description, product.name),
            contentScale = ContentScale.FillBounds,
            error = painterResource(id = R.drawable.ic_error),
            modifier = modifier
                .clickable {
                    previewProductName.value = product.name
                    previewProductImage.value = product.image
                    showDialog.value = true
                }
                .padding(8.dp)
                .width(60.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}

@Composable
private fun OrderedProductInfo(
    data: OrderedProduct,
    modifier: Modifier
) {
    Column(modifier = modifier.padding(start = 8.dp, end = 16.dp)) {
        Text(
            text = data.product.name.uppercase(), fontWeight = FontWeight.Normal, fontSize = 14.sp,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatRupiah(data.selledPrice),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
                else Color.Black
            )
            Spacer(modifier = modifier.width(2.dp))
            Text(
                text = stringResource(R.string.order_qty, data.amount),
                fontSize = 12.sp, fontWeight = FontWeight.Bold,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
                else Color.Black
            )
        }
    }
}

@Composable
private fun ProductInfo(
    product: DataProductOrder,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Text(
            text = stringResource(R.string.warehouse_info),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )
        Spacer(modifier = modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.standard_price),
                fontSize = 12.sp
            )
            Spacer(modifier = modifier.width(2.dp))
            Text(
                text = formatRupiah(product.standardPrice),
                fontSize = 12.sp, fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = stringResource(R.string.stock, product.stockInUnit, product.unit.lowercase()),
            fontSize = 12.sp
        )
        Text(
            text = stringResource(
                R.string.info_unit,
                product.unit.lowercase(),
                product.amountPerUnit
            ),
            fontSize = 11.sp, fontWeight = FontWeight.Light,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun OrderContent(
    viewModel: EditBarangPesananViewModel,
    product: DataProductOrder,
    changeAmount: MutableState<Int>,
    modifier: Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val currencyVisualTransformation = rememberCurrencyVisualTransformation(currency = "IDR")
    val selectedData = viewModel.selectedData.observeAsState().value

    var orderQty by rememberSaveable { mutableStateOf(product.orderQty.toString()) }
    var orderPrice by rememberSaveable { mutableStateOf(product.orderPrice.toString()) }
    var orderTotalPrice by rememberSaveable { mutableStateOf(product.orderTotalPrice.toString()) }

    LaunchedEffect(product) {
        orderQty = product.orderQty.toString()
        orderPrice = product.orderPrice.toString()
        orderTotalPrice = product.orderTotalPrice.toString()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (product.stockInUnit > 0) {
            if (!product.isChosen) {
                Button(
                    enabled = selectedData == null,
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        viewModel.enableOrder(product)
                    }) {
                    Icon(
                        Icons.Default.AddShoppingCart,
                        stringResource(R.string.choose),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = stringResource(R.string.choose),
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                }
            } else {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        viewModel.minusOrderQty(product)
                    }) {
                        Icon(
                            Icons.Default.Remove,
                            stringResource(R.string.minus_order_qty, product.name)
                        )
                    }
                    Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
                    OutlinedTextField(
                        label = { Text(stringResource(R.string.qty)) },
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        value = orderQty,
                        onValueChange = {
                            var newValue =
                                it.filter { it2 -> it2.isDigit() || it2 == '0' } // Only allow digits and leading zero
                            newValue = newValue.trimStart('0') // Remove leading zeros
                            newValue =
                                newValue.trim { it2 -> it2.isDigit().not() } // Remove non-digits

                            // Enforce minimum & maximum value
                            orderQty = if (newValue.isEmpty()) "0"
                            else if ((newValue.toIntOrNull()
                                    ?: 0) > QTY_MAX_VALUE
                            ) QTY_MAX_VALUE.toString() else newValue
                            viewModel.updateOrderQty(product, orderQty)
                        },
                        isError = product.orderQty == 0 || (changeAmount.value > product.stockInUnit),
                        modifier = modifier
                            .width(88.dp)
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    )
                    Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
                    IconButton(onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        viewModel.plusOrderQty(product)
                    }) {
                        Icon(
                            Icons.Default.Add,
                            stringResource(R.string.plus_order_qty, product.name)
                        )
                    }
                }
                OutlinedTextField(
                    label = { Text(stringResource(R.string.price)) },
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = currencyVisualTransformation,
                    value = orderPrice,
                    onValueChange = {
                        var newValue =
                            it.filter { it2 -> it2.isDigit() || it2 == '0' } // Only allow digits and leading zero
                        newValue = newValue.trimStart('0') // Remove leading zeros
                        newValue = newValue.trim { it2 -> it2.isDigit().not() } // Remove non-digits

                        // Enforce minimum & maximum value
                        orderPrice = if (newValue.isEmpty()) "0"
                        else if ((newValue.toLongOrNull()
                                ?: 0L) > MAX_VALUE
                        ) MAX_VALUE.toString() else newValue
                        viewModel.updateOrderPrice(product, orderPrice)
                    },
                    isError = product.orderPrice == 0L,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                )
                OutlinedTextField(
                    label = { Text(stringResource(R.string.total_price)) },
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = currencyVisualTransformation,
                    value = orderTotalPrice,
                    onValueChange = {
                        var newValue =
                            it.filter { it2 -> it2.isDigit() || it2 == '0' } // Only allow digits and leading zero
                        newValue = newValue.trimStart('0') // Remove leading zeros
                        newValue = newValue.trim { it2 -> it2.isDigit().not() } // Remove non-digits

                        // Enforce minimum & maximum value
                        orderTotalPrice = if (newValue.isEmpty()) "0"
                        else if ((newValue.toLongOrNull()
                                ?: 0L) > MAX_VALUE
                        ) MAX_VALUE.toString() else newValue
                        viewModel.updateOrderTotalPrice(product, orderTotalPrice)
                    },
                    isError = product.orderTotalPrice == 0L,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                )
                Spacer(modifier = modifier.height(16.dp))
                Button(
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        viewModel.disableOrder(product)
                    }, colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = stringResource(R.string.delete),
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                }
            }
        } else {
            Text(
                text = stringResource(R.string.stock_empty),
                fontSize = 12.sp, fontWeight = FontWeight.Normal, textAlign = TextAlign.Center,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    color = Color.Red
                ), modifier = modifier.padding(8.dp)
            )
            if (orderQty.toInt() > 0) {
                Spacer(modifier = modifier.height(16.dp))
                Button(
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        viewModel.disableOrder(product)
                    }, colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = stringResource(R.string.delete),
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                }
            }
        }
    }
}

private const val MAX_VALUE = 1_000_000_000L
private const val QTY_MAX_VALUE = 1_000

@Composable
@Preview(showBackground = true)
private fun UpdateOrderCardPreview() {
    JJSembakoTheme {
        val viewModel: EditBarangPesananViewModel = hiltViewModel()
        UpdateOrderCard(
            viewModel = viewModel,
            data = dataOrderedProducts[0],
            product = dataProductOrder,
            showDialog = remember { mutableStateOf(true) },
            previewProductName = remember { mutableStateOf("") },
            previewProductImage = remember { mutableStateOf("") },
            modifier = Modifier
        )
    }
}