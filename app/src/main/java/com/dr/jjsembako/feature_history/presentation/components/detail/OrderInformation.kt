package com.dr.jjsembako.feature_history.presentation.components.detail

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.dr.jjsembako.feature_history.domain.model.DataOrderHistoryCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OrderInformation(
    data: DataOrderHistoryCard,
    context: Context,
    clipboardManager: ClipboardManager,
    modifier: Modifier
) {
    val createdDate = convertTimestampToArray(data.createdAt)
    val toastCopiedIdMsg = stringResource(R.string.copied_invoice)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.order_information),
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
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(data.invoice))
                            Toast
                                .makeText(context, toastCopiedIdMsg, Toast.LENGTH_SHORT)
                                .show()
                        },
                        onLongClick = {
                            clipboardManager.setText(AnnotatedString(data.invoice))
                            Toast
                                .makeText(context, toastCopiedIdMsg, Toast.LENGTH_SHORT)
                                .show()
                        },
                        onClickLabel = toastCopiedIdMsg,
                        onLongClickLabel = toastCopiedIdMsg
                    ),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Row(
                    modifier = modifier.width(128.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(R.string.invoice_detail), fontSize = 12.sp,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                    Spacer(modifier = modifier.width(8.dp))
                    Icon(
                        Icons.Default.ContentCopy,
                        contentDescription = stringResource(R.string.copy_invoice),
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = modifier.size(12.dp)
                    )
                }
                Spacer(modifier = modifier.width(8.dp))
                Text(
                    text = data.invoice, fontSize = 12.sp,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                    modifier = modifier.weight(1f)
                )
            }
            Spacer(modifier = modifier.height(8.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(R.string.created), fontSize = 12.sp,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                    modifier = modifier.width(128.dp)
                )
                Spacer(modifier = modifier.width(8.dp))
                Text(
                    text = data.account.username, fontSize = 12.sp,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }
            Spacer(modifier = modifier.height(8.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(R.string.time_detail), fontSize = 12.sp,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                    modifier = modifier.width(128.dp)
                )
                Spacer(modifier = modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.time, createdDate[0], createdDate[1]),
                    fontSize = 12.sp,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }
        }

        Spacer(modifier = modifier.height(8.dp))
        Divider(
            modifier = modifier
                .fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = modifier.height(8.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            PaymentStatus(status = data.paymentStatus, modifier = modifier)
            Spacer(modifier = modifier.width(8.dp))
            OrderStatus(status = data.orderStatus, modifier = modifier)
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
private fun OrderInformationPreview() {
    JJSembakoTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            OrderInformation(
                data = dataOrderDataItem,
                context = LocalContext.current,
                clipboardManager = LocalClipboardManager.current,
                modifier = Modifier
            )
        }
    }
}