package com.dr.jjsembako.pesanan.presentation.components.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.dummy.dataOrderTimestamps
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.convertTimestampToArray
import com.dr.jjsembako.pesanan.domain.model.DataOrderTimestamps

@Composable
fun OrderTimestamps(data: DataOrderTimestamps, modifier: Modifier) {
    val createdDate = convertTimestampToArray(data.createdAt)
    val deliverDate =
        if (!data.deliverAt.isNullOrEmpty()) convertTimestampToArray(data.deliverAt) else arrayOf("", "")
    val finishedDate =
        if (!data.finishedAt.isNullOrEmpty()) convertTimestampToArray(data.finishedAt) else arrayOf("", "")

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(
            modifier = modifier
                .fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = modifier.height(8.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.time_order_created), fontSize = 12.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Text(
                text = stringResource(R.string.time, createdDate[0], createdDate[1]), fontSize = 12.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }
        Spacer(modifier = modifier.height(8.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.time_order_delivered), fontSize = 12.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Text(
                text = stringResource(R.string.time, deliverDate[0], deliverDate[1]), fontSize = 12.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }
        Spacer(modifier = modifier.height(8.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.time_order_finished), fontSize = 12.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Text(
                text = stringResource(R.string.time, finishedDate[0], finishedDate[1]), fontSize = 12.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }
        Spacer(modifier = modifier.height(8.dp))
        Divider(
            modifier = modifier
                .fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun OrderTimestampsPreview() {
    JJSembakoTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            OrderTimestamps(
                data = dataOrderTimestamps,
                modifier = Modifier
            )
        }
    }
}