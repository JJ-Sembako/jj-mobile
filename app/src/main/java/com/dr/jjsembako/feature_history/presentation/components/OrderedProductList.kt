package com.dr.jjsembako.feature_history.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.formatRupiah

@Composable
fun OrderedProductList(
    modifier: Modifier
) {
    val totalPrice = 125000L

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(
            modifier = modifier
                .fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = stringResource(R.string.ordered_product_detail),
            fontWeight = FontWeight.Bold, fontSize = 16.sp,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
        Divider(
            modifier = modifier
                .fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary
        )

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            OrderedProductItem(modifier = modifier)
            Spacer(modifier = modifier.height(8.dp))
            OrderedProductItem(modifier = modifier)
            Spacer(modifier = modifier.height(8.dp))
            OrderedProductItem(modifier = modifier)
            Spacer(modifier = modifier.height(8.dp))
            OrderedProductItem(modifier = modifier)
            Spacer(modifier = modifier.height(8.dp))
            OrderedProductItem(modifier = modifier)
            Spacer(modifier = modifier.height(8.dp))
        }

        Divider(
            modifier = modifier
                .fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(R.string.total_price_order),
                fontSize = 12.sp, fontWeight = FontWeight.Normal,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = formatRupiah(totalPrice), fontWeight = FontWeight.Bold, fontSize = 14.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }
        Divider(
            modifier = modifier
                .fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun OrderedProductListPreview() {
    JJSembakoTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OrderedProductList(
                modifier = Modifier
            )
        }
    }
}