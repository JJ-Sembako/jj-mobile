package com.dr.jjsembako.feature_order.presentation.components

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
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
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
import coil.compose.AsyncImage
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.model.DataProductOrder
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.formatRupiah

@Composable
fun ProductOnOrder(
    product: DataProductOrder,
    modifier: Modifier
) {
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
        OrderContent(product = product, modifier = modifier)
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

@Composable
private fun OrderContent(
    product: DataProductOrder,
    modifier: Modifier
) {
    val focusManager = LocalFocusManager.current
    var orderQty by rememberSaveable { mutableStateOf("") }
    var orderPrice by rememberSaveable { mutableStateOf("") }
    var orderTotalPrice by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(product.isChosen) {
        orderQty = if (product.isChosen) product.orderQty.toString()
        else "0"
    }
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
                Button(onClick = { enableOrder(product) }) {
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
                    IconButton(onClick = { minusOrderQty(product) }) {
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
                            updateOrderQty(product, orderQty)
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    )
                    Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
                    IconButton(onClick = { plusOrderQty(product) }) {
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
                    value = orderPrice,
                    onValueChange = {
                        val newValue = it.toIntOrNull() ?: 0
                        orderPrice = maxOf(newValue, 0).toString()
                        updateOrderPrice(product, orderPrice)
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
                    value = orderTotalPrice,
                    onValueChange = {
                        val newValue = it.toIntOrNull() ?: 0
                        orderTotalPrice = maxOf(newValue, 0).toString()
                        updateOrderTotalPrice(product, orderTotalPrice)
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                )
            }
        } else {
            disableOrder(product)
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

private fun updateOrderTotalPrice(product: DataProductOrder, total: String) {
    if (total.isNotEmpty()) {
        val orderTotalPrice = total.toLong()
        if (orderTotalPrice > 0) {
            product.orderTotalPrice = orderTotalPrice
            product.orderPrice = product.orderTotalPrice / product.orderQty
        } else {
            disableOrder(product)
        }
    } else {
        disableOrder(product)
    }
}

private fun updateOrderPrice(product: DataProductOrder, price: String) {
    if (price.isNotEmpty()) {
        val orderPrice = price.toLong()
        if (orderPrice > 0) {
            product.orderPrice = orderPrice
            product.orderTotalPrice = product.orderQty * product.standardPrice
        } else {
            disableOrder(product)
        }
    } else {
        disableOrder(product)
    }
}

private fun updateOrderQty(product: DataProductOrder, qty: String) {
    if (qty.isNotEmpty()) {
        val orderQty = qty.toInt()
        if (orderQty > 0) {
            product.orderQty = orderQty
            product.orderTotalPrice = product.orderQty * product.standardPrice
        } else {
            disableOrder(product)
        }
    } else {
        disableOrder(product)
    }
}

private fun disableOrder(product: DataProductOrder) {
    product.orderQty = 0
    product.orderPrice = product.standardPrice
    product.orderTotalPrice = 0
    product.isChosen = false
}

private fun enableOrder(product: DataProductOrder) {
    product.orderQty = 1
    product.orderPrice = product.standardPrice
    product.orderTotalPrice = product.orderQty * product.standardPrice
    product.isChosen = true
}

private fun minusOrderQty(product: DataProductOrder) {
    if (product.orderQty > 0) {
        product.orderQty -= 1
        if (product.orderQty == 0) disableOrder(product)
        else product.orderTotalPrice = product.orderQty * product.standardPrice
    }
}

private fun plusOrderQty(product: DataProductOrder) {
    product.orderQty += 1
    product.orderTotalPrice = product.orderQty * product.standardPrice
}

@Composable
@Preview(showBackground = true)
private fun ProductOnOrderPreview() {
    JJSembakoTheme {
        ProductOnOrder(
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