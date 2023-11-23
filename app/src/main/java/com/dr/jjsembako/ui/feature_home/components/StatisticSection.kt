package com.dr.jjsembako.ui.feature_home.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.utils.formatTotal
import com.dr.jjsembako.ui.theme.JJSembakoTheme

@Composable
fun StatisticSection(totalPesanan: Int = 0, totalBarang: Int = 0) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatisticCard(totalPesanan, true)
        StatisticCard(totalBarang, false)
    }
}

@Composable
private fun StatisticCard(total: Int, isPesanan: Boolean) {
    val text = if (isPesanan) {
        stringResource(R.string.pesanan_dibuat_bulan_ini)
    } else stringResource(R.string.barang_dijual_bulan_ini)
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .width(160.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp),
                fontSize = 14.sp
            )
            Text(
                text = formatTotal(total),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun StatisticCardPesananPreview() {
    JJSembakoTheme {
        StatisticCard(1813, true)
    }
}

@Composable
@Preview(showBackground = true)
fun StatisticCardBarangPreview() {
    JJSembakoTheme {
        StatisticCard(12345, false)
    }
}

@Composable
@Preview(showBackground = true)
fun StatisticSectionPreview() {
    JJSembakoTheme {
        StatisticSection(1813, 12345)
    }
}