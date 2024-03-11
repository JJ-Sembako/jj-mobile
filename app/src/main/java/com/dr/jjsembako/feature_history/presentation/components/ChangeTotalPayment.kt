package com.dr.jjsembako.feature_history.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.presentation.theme.StatusDeliveredText
import com.dr.jjsembako.core.utils.formatRupiah

@Composable
fun ChangeTotalPayment(
    orderCost: Long,
    changeCost: Long,
    modifier: Modifier,
    isForUpdate: Boolean = false,
    changeQty: Int? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        ChangePaymentHeader(modifier)
        ChangePaymentContent(orderCost, changeCost, modifier, isForUpdate, changeQty)
    }
}

@Composable
private fun ChangePaymentContent(
    orderCost: Long,
    changeCost: Long,
    modifier: Modifier,
    isForUpdate: Boolean,
    changeQty: Int? = null
) {
    val finalCost = orderCost + changeCost
    val changeCostText =
        if (changeCost >= 0L) "+ ${formatRupiah(changeCost)}" else "- ${formatRupiah(-changeCost)}"

    Spacer(modifier = modifier.height(16.dp))
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.original_cost),
            fontSize = 12.sp, fontWeight = FontWeight.Normal,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = formatRupiah(orderCost), fontWeight = FontWeight.Bold, fontSize = 14.sp,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )
    }
    Spacer(modifier = modifier.height(8.dp))
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.change_cost),
            fontSize = 12.sp, fontWeight = FontWeight.Normal,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = changeCostText, fontWeight = FontWeight.Bold, fontSize = 14.sp,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            color = if (changeCost >= 0L) StatusDeliveredText else Color.Red
        )
    }
    Spacer(modifier = modifier.height(8.dp))
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.final_cost),
            fontSize = 12.sp, fontWeight = FontWeight.Normal,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = formatRupiah(finalCost), fontWeight = FontWeight.Bold, fontSize = 14.sp,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )
    }
    if (isForUpdate && changeQty != null) {
        Spacer(modifier = modifier.height(8.dp))
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(R.string.change_qty),
                fontSize = 12.sp, fontWeight = FontWeight.Normal,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = changeQty.toString(), fontWeight = FontWeight.Bold, fontSize = 14.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }
    }
    Spacer(modifier = modifier.height(16.dp))
    Divider(
        modifier = modifier
            .fillMaxWidth()
            .width(1.dp), color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
private fun ChangePaymentHeader(modifier: Modifier) {
    Divider(
        modifier = modifier
            .fillMaxWidth()
            .width(1.dp), color = MaterialTheme.colorScheme.tertiary
    )
    Column(modifier = modifier.padding(horizontal = 8.dp, vertical = 16.dp)) {
        Text(
            text = stringResource(id = R.string.cost_information), fontSize = 14.sp
        )
    }
    Divider(
        modifier = modifier
            .fillMaxWidth()
            .width(1.dp), color = MaterialTheme.colorScheme.tertiary
    )
}

@Preview(showBackground = true)
@Composable
private fun PNRTotalPaymentPreview() {
    JJSembakoTheme {
        ChangeTotalPayment(
            orderCost = 1_500_000L,
            changeCost = -125_000L,
            modifier = Modifier
        )
    }
}