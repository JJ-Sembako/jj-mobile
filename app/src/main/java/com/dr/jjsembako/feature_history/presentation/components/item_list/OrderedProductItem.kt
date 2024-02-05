package com.dr.jjsembako.feature_history.presentation.components.item_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.dummy.dataOrderToProductsItem
import com.dr.jjsembako.core.data.remote.response.order.OrderToProductsItem
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.formatRupiah

@Composable
fun OrderedProductItem(
    data: OrderToProductsItem,
    showDialog: MutableState<Boolean>,
    modifier: Modifier
) {
    val expanded = remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp)
    ) {
        Option(expanded, showDialog, modifier)
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProductImage(data, modifier)
            OrderedProductInfo(data, modifier)
        }
        Spacer(modifier = modifier.height(4.dp))
    }
}

@Composable
private fun Option(
    expanded: MutableState<Boolean>,
    showDialog: MutableState<Boolean>,
    modifier: Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = modifier.height(8.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { expanded.value = !expanded.value }) {
                Icon(
                    Icons.Default.MoreVert,
                    stringResource(R.string.menu),
                    tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.tertiary,
                )
            }
            Box(modifier = modifier.padding(top = 16.dp), contentAlignment = Alignment.TopEnd) {
                DropdownMenu(
                    modifier = modifier.width(180.dp),
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.del_product_order)) },
                        onClick = {
                            expanded.value = !expanded.value
                            showDialog.value = !showDialog.value
                        })
                }
            }
        }
    }
}

@Composable
private fun ProductImage(
    data: OrderToProductsItem,
    modifier: Modifier
) {
    if (data.product.image.isEmpty() || data.product.image.contains("default")) {
        Image(
            painter = painterResource(id = R.drawable.ic_default),
            contentDescription = stringResource(R.string.product_description, data.product.name),
            contentScale = ContentScale.Crop,
            modifier = modifier
                .padding(8.dp)
                .width(30.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    } else {
        AsyncImage(
            model = data.product.image,
            contentDescription = stringResource(R.string.product_description, data.product.name),
            contentScale = ContentScale.FillBounds,
            error = painterResource(id = R.drawable.ic_error),
            modifier = modifier
                .padding(8.dp)
                .width(30.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}

@Composable
private fun OrderedProductInfo(
    data: OrderToProductsItem,
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
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = modifier.width(2.dp))
            Text(
                text = stringResource(R.string.order_qty, data.amount),
                fontSize = 12.sp, fontWeight = FontWeight.Bold,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun OrderedProductItemPreview() {
    JJSembakoTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OrderedProductItem(
                data = dataOrderToProductsItem[0],
                showDialog = remember { mutableStateOf(true) },
                modifier = Modifier
            )
        }
    }
}