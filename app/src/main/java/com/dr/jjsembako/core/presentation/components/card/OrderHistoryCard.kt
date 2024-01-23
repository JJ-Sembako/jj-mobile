package com.dr.jjsembako.core.presentation.components.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun OrderHistoryCard (
    onNavigateToDetail: (String) -> Unit,
    modifier: Modifier
) {
    val id = "20240121-ABC123"
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp)
            .clickable { onNavigateToDetail("") },
    ){
        Column(
            modifier = modifier
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.id, id), fontWeight = FontWeight.Bold, fontSize = 14.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Text(
                text = stringResource(R.string.time, "23 Jan 2024", "12:25" ), fontSize = 12.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Text(
                text = formatRupiah(450000L),
                fontSize = 12.sp
            )
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = "Toko Aji Sakti", fontSize = 12.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Text(
                text = "Budi Waluyo", fontSize = 12.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Text(
                text = "Jl. Slamet Riyadi No. 46, Solo, Kota Surakarta", fontSize = 12.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun OrderHistoryPreview() {
    JJSembakoTheme {
        OrderHistoryCard (
            onNavigateToDetail = {},
            modifier = Modifier
        )
    }
}