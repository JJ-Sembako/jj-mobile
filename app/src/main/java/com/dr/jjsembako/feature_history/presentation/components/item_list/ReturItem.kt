package com.dr.jjsembako.feature_history.presentation.components.item_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
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
import com.dr.jjsembako.core.presentation.components.utils.ReturStatus
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.formatRupiah

@Composable
fun ReturItem(
    modifier: Modifier
) {
    val expanded = remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp)
    ) {
        StatusAndOption(expanded, modifier)
        ProductOnReturnedItem(modifier)
        DividerInfo(modifier)
        ProductSubstituteItem(modifier)
    }
}

@Composable
private fun StatusAndOption(
    expanded: MutableState<Boolean>,
    modifier: Modifier
) {
    Spacer(modifier = modifier.height(8.dp))
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        ReturStatus(status = 1, modifier = modifier)
        IconButton(onClick = { expanded.value = !expanded.value }) {
            Icon(
                Icons.Default.MoreVert,
                stringResource(R.string.menu),
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
        Box(modifier = modifier.padding(top = 16.dp), contentAlignment = Alignment.TopEnd) {
            DropdownMenu(
                modifier = modifier.width(144.dp),
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }) {
                DropdownMenuItem(
                    text = { stringResource(R.string.cancel_retur) },
                    onClick = { /*TODO*/ })
            }
        }
    }
    Spacer(modifier = modifier.height(8.dp))
}

@Composable
private fun DividerInfo(
    modifier: Modifier
) {
    Spacer(modifier = modifier.height(8.dp))
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Divider(
            modifier = modifier
                .width(64.dp), color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = modifier.width(8.dp))
        Text(
            text = stringResource(R.string.replaced),
            fontSize = 12.sp, fontWeight = FontWeight.Normal,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )
        Spacer(modifier = modifier.width(8.dp))
        Divider(
            modifier = modifier
                .width(64.dp), color = MaterialTheme.colorScheme.tertiary
        )
    }
    Spacer(modifier = modifier.height(8.dp))
}

@Composable
private fun ProductOnReturnedItem(
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProductImage(modifier)
        ProductInfo(true, modifier)
    }
}

@Composable
private fun ProductSubstituteItem(
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProductImage(modifier)
        ProductInfo(false, modifier)
    }
}

@Composable
private fun ProductImage(
    modifier: Modifier
) {
    val image = ""
    val name = "Sari Roti Tawar"

    if (image.isEmpty() || image.contains("default")) {
        Image(
            painter = painterResource(id = R.drawable.ic_default),
            contentDescription = stringResource(R.string.product_description, name),
            contentScale = ContentScale.Crop,
            modifier = modifier
                .padding(8.dp)
                .width(60.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    } else {
        AsyncImage(
            model = image,
            contentDescription = stringResource(R.string.product_description, name),
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
    isReturned: Boolean,
    modifier: Modifier
) {
    val name = "Sari Roti Tawar"
    name.uppercase()
    val orderQty = 10
    val sellPrice = 15000L

    Column(modifier = modifier.padding(start = 8.dp, end = 16.dp)) {
        Text(
            text = name, fontWeight = FontWeight.Normal, fontSize = 14.sp,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (isReturned) "-${formatRupiah(sellPrice)}" else formatRupiah(sellPrice),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isReturned) Color.Red else MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = modifier.width(2.dp))
            Text(
                text = stringResource(R.string.order_qty, orderQty),
                fontSize = 12.sp, fontWeight = FontWeight.Bold,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ReturItemPreview() {
    JJSembakoTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReturItem(
                modifier = Modifier
            )
        }
    }
}