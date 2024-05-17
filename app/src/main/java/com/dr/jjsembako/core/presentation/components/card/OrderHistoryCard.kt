package com.dr.jjsembako.core.presentation.components.card

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.dummy.dataOrderDataItem
import com.dr.jjsembako.core.presentation.components.utils.OrderStatus
import com.dr.jjsembako.core.presentation.components.utils.PaymentStatus
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.convertTimestampToArray
import com.dr.jjsembako.core.utils.formatRupiah
import com.dr.jjsembako.pesanan.domain.model.DataOrderHistoryCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OrderHistoryCard(
    data: DataOrderHistoryCard,
    context: Context,
    onNavigateToDetail: (String) -> Unit,
    clipboardManager: ClipboardManager,
    modifier: Modifier
) {
    val toastCopiedIdMsg = stringResource(R.string.copied_invoice)
    val createdDate = convertTimestampToArray(data.createdAt)

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp)
            .combinedClickable(
                onClick = { onNavigateToDetail(data.id) },
                onLongClick = {
                    clipboardManager.setText(AnnotatedString(data.invoice))
                    Toast
                        .makeText(context, toastCopiedIdMsg, Toast.LENGTH_SHORT)
                        .show()
                },
                onLongClickLabel = toastCopiedIdMsg
            )
    ) {
        Text(
            text = stringResource(R.string.invoice, data.invoice),
            fontWeight = FontWeight.Bold, fontSize = 14.sp,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
        Divider(
            modifier = modifier
                .fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = stringResource(R.string.created_by, data.account.username), fontSize = 12.sp,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            modifier = modifier.padding(vertical = 4.dp, horizontal = 16.dp)
        )
        Divider(
            modifier = modifier
                .fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary
        )
        Column(
            modifier = modifier
                .padding(16.dp)
        ) {
            Text(
                text = data.customer.shopName, fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Text(
                text = data.customer.name, fontSize = 12.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Text(
                text = data.customer.address, fontSize = 12.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }

        Divider(
            modifier = modifier
                .fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            PaymentStatus(status = data.paymentStatus, modifier = modifier)
            Spacer(modifier = modifier.width(8.dp))
            OrderStatus(status = data.orderStatus, modifier = modifier)
        }

        Divider(
            modifier = modifier
                .fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatRupiah(data.totalPrice),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Text(
                text = stringResource(R.string.time, createdDate[0], createdDate[1]),
                fontSize = 12.sp,
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
                data = dataOrderDataItem,
                context = LocalContext.current,
                onNavigateToDetail = {},
                clipboardManager = LocalClipboardManager.current,
                modifier = Modifier
            )
        }
    }
}