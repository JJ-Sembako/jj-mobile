package com.dr.jjsembako.pesanan.presentation.components.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.dr.jjsembako.core.data.dummy.dataSubstituteProduct
import com.dr.jjsembako.pesanan.domain.model.SubstituteProduct
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.formatRupiah
import com.dr.jjsembako.core.utils.rememberCurrencyVisualTransformation
import com.dr.jjsembako.pesanan.presentation.retur.select_substitute.PilihPenggantiReturViewModel

@Composable
fun SelectSubsRCard(
    viewModel: PilihPenggantiReturViewModel,
    data: SubstituteProduct,
    showDialog: MutableState<Boolean>,
    previewProductName: MutableState<String>,
    previewProductImage: MutableState<String>,
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
            ProductImage(data, showDialog, previewProductName, previewProductImage, modifier)
            Spacer(modifier = modifier.width(16.dp))
            ProductInfo(data, modifier)
        }
        OrderContent(viewModel, data, modifier)
    }
}

@Composable
private fun ProductImage(
    product: SubstituteProduct,
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
private fun ProductInfo(
    product: SubstituteProduct,
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
    viewModel: PilihPenggantiReturViewModel,
    product: SubstituteProduct,
    modifier: Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val currencyVisualTransformation = rememberCurrencyVisualTransformation(currency = "IDR")
    val selectedData = viewModel.selectedData.observeAsState().value
    var selledPrice by rememberSaveable { mutableStateOf(product.selledPrice.toString()) }

    LaunchedEffect(product) {
        selledPrice = product.selledPrice.toString()
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
                OutlinedTextField(
                    label = { Text(stringResource(R.string.price)) },
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = currencyVisualTransformation,
                    value = selledPrice,
                    onValueChange = {
                        var newValue =
                            it.filter { it2 -> it2.isDigit() || it2 == '0' } // Only allow digits and leading zero
                        newValue = newValue.trimStart('0') // Remove leading zeros
                        newValue = newValue.trim { it2 -> it2.isDigit().not() } // Remove non-digits

                        // Enforce minimum & maximum value
                        selledPrice = if (newValue.isEmpty()) "0"
                        else if ((newValue.toLongOrNull()
                                ?: 0L) > MAX_VALUE
                        ) MAX_VALUE.toString() else newValue
                        viewModel.updateOrderPrice(product, selledPrice)
                    },
                    isError = product.selledPrice == 0L,
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
            if (product.isChosen) {
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

@Composable
@Preview(showBackground = true)
private fun SelectSubsRCardPreview() {
    JJSembakoTheme {
        val viewModel: PilihPenggantiReturViewModel = hiltViewModel()
        SelectSubsRCard(
            viewModel = viewModel,
            data = dataSubstituteProduct,
            showDialog = remember { mutableStateOf(true) },
            previewProductName = remember { mutableStateOf("") },
            previewProductImage = remember { mutableStateOf("") },
            modifier = Modifier
        )
    }
}