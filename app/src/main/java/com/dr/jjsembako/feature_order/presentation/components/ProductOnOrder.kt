package com.dr.jjsembako.feature_order.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.model.DataProductOrder
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.presentation.theme.RedContainer
import com.dr.jjsembako.core.utils.formatRupiah
import com.dr.jjsembako.core.utils.rememberCurrencyVisualTransformation
import com.dr.jjsembako.feature_order.presentation.select_product.PilihBarangViewModel

@Composable
fun ProductOnOrder(
    pilihBarangViewModel: PilihBarangViewModel,
    product: DataProductOrder,
    modifier: Modifier
) {
    if (product.isChosen && (product.orderTotalPrice == 0L || product.orderQty > product.stockInUnit)) {
        OutlinedCard(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .padding(horizontal = 8.dp),
            border = BorderStroke(1.dp, Color.Red),
            colors = CardDefaults.cardColors(contentColor = RedContainer)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ProductImage(product = product, modifier = modifier)
                Spacer(modifier = modifier.width(16.dp))
                ProductInfo(product = product, modifier = modifier)
            }
            OrderContent(pilihBarangViewModel, product, modifier)
        }
    } else {
        OutlinedCard(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .padding(horizontal = 8.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ProductImage(product = product, modifier = modifier)
                Spacer(modifier = modifier.width(16.dp))
                ProductInfo(product = product, modifier = modifier)
            }
            OrderContent(pilihBarangViewModel, product, modifier)
        }
    }
}

@Composable
private fun ProductImage(
    product: DataProductOrder,
    modifier: Modifier
) {
    if (product.image.isEmpty() || product.image.contains("default")) {
        Image(
            painter = painterResource(id = R.drawable.ic_default),
            contentDescription = stringResource(R.string.product_description, product.name),
            contentScale = ContentScale.Crop,
            modifier = modifier
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
                .padding(8.dp)
                .width(60.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}

@Composable
private fun ProductInfo(
    product: DataProductOrder,
    modifier: Modifier
) {
    Column {
        Text(
            text = product.name, fontWeight = FontWeight.Bold, fontSize = 14.sp,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )
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
    pilihBarangViewModel: PilihBarangViewModel,
    product: DataProductOrder,
    modifier: Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val currencyVisualTransformation = rememberCurrencyVisualTransformation(currency = "IDR")

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
                Button(onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    pilihBarangViewModel.enableOrder(product)
                }) {
                    Icon(
                        Icons.Default.AddShoppingCart,
                        stringResource(R.string.add_to_cart),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = stringResource(R.string.add_to_cart),
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
                        pilihBarangViewModel.minusOrderQty(product)
                    }) {
                        Icon(
                            Icons.Default.Remove,
                            stringResource(R.string.minus_order_qty, product.name),
                            tint = MaterialTheme.colorScheme.onPrimary
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
                            val newValue = it.toIntOrNull() ?: 0
                            orderQty = maxOf(newValue, 0).toString()
                            pilihBarangViewModel.updateOrderQty(product, orderQty)
                        },
                        modifier = modifier
                            .width(88.dp)
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    )
                    Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
                    IconButton(onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        pilihBarangViewModel.plusOrderQty(product)
                    }) {
                        Icon(
                            Icons.Default.Add,
                            stringResource(R.string.plus_order_qty, product.name),
                            tint = MaterialTheme.colorScheme.onPrimary
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
                        pilihBarangViewModel.updateOrderPrice(product, orderPrice)
                    },
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
                        pilihBarangViewModel.updateOrderTotalPrice(product, orderTotalPrice)
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                )
                Spacer(modifier = modifier.height(16.dp))
                Button(
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        pilihBarangViewModel.disableOrder(product)
                    }, colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        stringResource(R.string.add_to_cart),
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
                fontSize = 12.sp, fontWeight = FontWeight.Normal,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    color = Color.Red
                )
            )
        }
    }
}

private const val MAX_VALUE = 1_000_000_000L

@Composable
@Preview(showBackground = true)
private fun ProductOnOrderPreview() {
    JJSembakoTheme {
        val pilihBarangViewModel: PilihBarangViewModel = hiltViewModel()
        ProductOnOrder(
            pilihBarangViewModel = pilihBarangViewModel,
            product = DataProductOrder(
                id = "bc3bbd9e",
                name = "Air Cahaya",
                image = "",
                category = "Air Mineral",
                unit = "karton",
                standardPrice = 55000,
                amountPerUnit = 16,
                stockInPcs = 256,
                stockInUnit = 16,
                stockInPcsRemaining = 0
            ),
            modifier = Modifier
        )
    }
}