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
import com.dr.jjsembako.core.data.dummy.dataSelectPNRItem
import com.dr.jjsembako.core.data.model.SelectPNRItem
import com.dr.jjsembako.core.data.remote.response.product.Product
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.formatRupiah
import com.dr.jjsembako.feature_history.presentation.potong_nota.select_product.PilihBarangPotongNotaViewModel

@Composable
fun SelectOrderPNCard(
    viewModel: PilihBarangPotongNotaViewModel,
    data: SelectPNRItem,
    showDialog: MutableState<Boolean>,
    previewProductName: MutableState<String>,
    previewProductImage: MutableState<String>,
    modifier: Modifier
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp),
        border = if (data.isChosen && (data.amountSelected == 0 || (data.amountSelected > data.actualAmount))) {
            BorderStroke(3.dp, Color.Red)
        } else CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProductImage(
                data.product, showDialog,
                previewProductName, previewProductImage, modifier
            )
            OrderedProductInfo(data, modifier)
        }
        OrderContent(viewModel, data, modifier)
    }
}

@Composable
private fun ProductImage(
    product: Product,
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
    data: SelectPNRItem,
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun OrderContent(
    viewModel: PilihBarangPotongNotaViewModel,
    data: SelectPNRItem,
    modifier: Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val selectedData = viewModel.selectedData.observeAsState().value
    var selectedAmount by rememberSaveable { mutableStateOf(data.amountSelected.toString()) }

    LaunchedEffect(data) {
        selectedAmount = data.amountSelected.toString()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (data.status == 0) {
            if (!data.isChosen && (selectedData?.id != data.id)) {
                Button(
                    enabled = selectedData == null,
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        viewModel.enableChoose(data)
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
                        viewModel.minusSelectedAmount(data)
                    }) {
                        Icon(
                            Icons.Default.Remove,
                            stringResource(R.string.minus_amount_selected, data.product.name)
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
                        value = selectedAmount,
                        onValueChange = {
                            var newValue =
                                it.filter { it2 -> it2.isDigit() || it2 == '0' } // Only allow digits and leading zero
                            newValue = newValue.trimStart('0') // Remove leading zeros
                            newValue =
                                newValue.trim { it2 -> it2.isDigit().not() } // Remove non-digits

                            // Enforce minimum & maximum value
                            selectedAmount = if (newValue.isEmpty()) "0"
                            else if ((newValue.toIntOrNull()
                                    ?: 0) > QTY_MAX_VALUE
                            ) QTY_MAX_VALUE.toString() else newValue
                            viewModel.updateSelectedAmount(data, selectedAmount)
                        },
                        isError = data.amountSelected == 0 || (data.amountSelected > data.actualAmount),
                        modifier = modifier
                            .width(88.dp)
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    )
                    Spacer(modifier = modifier.size(ButtonDefaults.IconSpacing))
                    IconButton(onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        viewModel.plusSelectedAmount(data)
                    }) {
                        Icon(
                            Icons.Default.Add,
                            stringResource(R.string.plus_amount_selected, data.product.name)
                        )
                    }
                }
                Spacer(modifier = modifier.height(16.dp))
                Button(
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        viewModel.disableChoose(data)
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
                text = stringResource(R.string.already_pnr),
                fontSize = 12.sp, fontWeight = FontWeight.Normal, textAlign = TextAlign.Center,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    color = Color.Red
                ), modifier = modifier.padding(8.dp)
            )
            if (selectedAmount.toInt() > 0) {
                Spacer(modifier = modifier.height(16.dp))
                Button(
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        viewModel.disableChoose(data)
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

private const val QTY_MAX_VALUE = 1_000

@Composable
@Preview(showBackground = true)
private fun SelectOrderPNCardPreview() {
    JJSembakoTheme {
        val viewModel: PilihBarangPotongNotaViewModel = hiltViewModel()
        SelectOrderPNCard(
            viewModel = viewModel,
            data = dataSelectPNRItem,
            showDialog = remember { mutableStateOf(true) },
            previewProductName = remember { mutableStateOf("") },
            previewProductImage = remember { mutableStateOf("") },
            modifier = Modifier
        )
    }
}