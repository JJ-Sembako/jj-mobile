package com.dr.jjsembako.feature_history.presentation.components.item_list

import androidx.compose.foundation.Image
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
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.formatRupiah

@Composable
fun PotongNotaItem(
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
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProductImage(modifier = modifier)
            PotongNotaInfo(modifier = modifier)
        }
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
        Text(
            text = "Status",
            fontSize = 12.sp, fontWeight = FontWeight.Normal,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )
        IconButton(onClick = { expanded.value = !expanded.value }) {
            Icon(
                Icons.Default.MoreVert,
                stringResource(R.string.menu),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        Box(modifier = modifier.padding(top = 16.dp), contentAlignment = Alignment.TopEnd) {
            DropdownMenu(
                modifier = modifier.width(144.dp),
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }) {
                DropdownMenuItem(
                    text = { stringResource(R.string.cancel_potong_nota) },
                    onClick = { /*TODO*/ })
            }
        }
    }
    Spacer(modifier = modifier.height(8.dp))
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
private fun PotongNotaInfo(
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
                text = "-${formatRupiah(sellPrice)}", fontWeight = FontWeight.Bold, fontSize = 14.sp,
                color = Color.Red
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
private fun PotongNotaItemPreview() {
    JJSembakoTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PotongNotaItem(
                modifier = Modifier
            )
        }
    }
}