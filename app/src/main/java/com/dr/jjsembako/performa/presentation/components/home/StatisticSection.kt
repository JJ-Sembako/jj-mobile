package com.dr.jjsembako.performa.presentation.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun StatisticSection(
    totalPesanan: Int = 0, totalBarang: Int = 0,
    isErrorInit: Boolean = false,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatisticCard(totalPesanan, true, isErrorInit, modifier)
        StatisticCard(totalBarang, false, isErrorInit, modifier)
    }
}

@Composable
private fun StatisticCard(
    total: Int, isPesanan: Boolean,
    isErrorInit: Boolean = false,
    modifier: Modifier
) {
    val text = if (isPesanan) {
        stringResource(R.string.orders_made_this_month)
    } else stringResource(R.string.items_sold_this_month)
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = modifier
                .width(160.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                modifier = modifier.padding(bottom = 8.dp),
                fontSize = 14.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            if(isErrorInit) {
                Text(
                    text = stringResource(R.string.error_string),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold, fontSize = 24.sp,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            } else {
                Text(
                    text = total.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun StatisticCardPesananPreview() {
    JJSembakoTheme {
        StatisticCard(1813, true, modifier = Modifier)
    }
}

@Composable
@Preview(showBackground = true)
private fun StatisticCardBarangPreview() {
    JJSembakoTheme {
        StatisticCard(12345, false, modifier = Modifier)
    }
}

@Composable
@Preview(showBackground = true)
private fun StatisticSectionPreview() {
    JJSembakoTheme {
        StatisticSection(1813, 12345, modifier = Modifier)
    }
}