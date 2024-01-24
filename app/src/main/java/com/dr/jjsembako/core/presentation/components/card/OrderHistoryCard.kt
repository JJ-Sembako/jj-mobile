package com.dr.jjsembako.core.presentation.components.card

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.components.utils.OrderStatus
import com.dr.jjsembako.core.presentation.components.utils.PaymentStatus
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.formatRupiah

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OrderHistoryCard(
    onNavigateToDetail: (String) -> Unit,
    clipboardManager: ClipboardManager,
    modifier: Modifier
) {
    val id = "20240121-ABC123"

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp)
            .combinedClickable(
                onClick = { onNavigateToDetail("") },
                onLongClick = {
                    clipboardManager.setText(AnnotatedString(id))
                },
                onLongClickLabel = stringResource(R.string.copy_id)
            )
    ) {
        Text(
            text = stringResource(R.string.id, id),
            fontWeight = FontWeight.Bold, fontSize = 14.sp,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
        Divider(
            modifier = modifier
                .fillMaxWidth()
                .width(1.dp), color = MaterialTheme.colorScheme.tertiary
        )

        Column(
            modifier = modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Toko Aji Sakti", fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
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

        Divider(
            modifier = modifier
                .fillMaxWidth()
                .width(1.dp), color = MaterialTheme.colorScheme.tertiary
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            PaymentStatus(status = 0, modifier = modifier)
            Spacer(modifier = modifier.width(8.dp))
            OrderStatus(status = 0, modifier = modifier)
        }

        Divider(
            modifier = modifier
                .fillMaxWidth()
                .width(1.dp), color = MaterialTheme.colorScheme.tertiary
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatRupiah(450000L), fontSize = 12.sp, fontWeight = FontWeight.Bold,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Text(
                text = stringResource(R.string.time, "23 Jan 2024", "12:25"), fontSize = 12.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun OrderHistoryPreview() {
    JJSembakoTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OrderHistoryCard(
                onNavigateToDetail = {},
                clipboardManager = LocalClipboardManager.current,
                modifier = Modifier
            )
        }
    }
}