package com.dr.jjsembako.core.presentation.components.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.dr.jjsembako.core.presentation.theme.OrderDeliveredBg
import com.dr.jjsembako.core.presentation.theme.OrderDeliveredText
import com.dr.jjsembako.core.presentation.theme.OrderFinishedBg
import com.dr.jjsembako.core.presentation.theme.OrderFinishedText
import com.dr.jjsembako.core.presentation.theme.OrderPackedBg
import com.dr.jjsembako.core.presentation.theme.OrderPackedText
import com.dr.jjsembako.core.presentation.theme.OrderWaitConfirmBg
import com.dr.jjsembako.core.presentation.theme.OrderWaitConfirmText
import com.dr.jjsembako.core.presentation.theme.StatusNotFoundBg
import com.dr.jjsembako.core.presentation.theme.StatusNotFoundText

@Composable
fun OrderStatus(
    status: Int,
    modifier: Modifier
) {

    val statusText = when (status) {
        0 -> stringResource(R.string.order_wait_confirm)
        1 -> stringResource(R.string.order_packed)
        2 -> stringResource(R.string.order_delivered)
        3 -> stringResource(R.string.order_finished)
        else -> stringResource(R.string.status_not_found)
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(
                when (status) {
                    0 -> OrderWaitConfirmBg
                    1 -> OrderPackedBg
                    2 -> OrderDeliveredBg
                    3 -> OrderFinishedBg
                    else -> StatusNotFoundBg
                }
            )
            .padding(4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = statusText,
            fontWeight = FontWeight.Bold, fontSize = 12.sp,
            textAlign = TextAlign.Center,
            style = TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                color = when (status) {
                    0 -> OrderWaitConfirmText
                    1 -> OrderPackedText
                    2 -> OrderDeliveredText
                    3 -> OrderFinishedText
                    else -> StatusNotFoundText
                }
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun OrderStatusPreview() {
    JJSembakoTheme {
        Column(
            modifier = Modifier.size(150.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OrderStatus(
                status = 0,
                modifier = Modifier
            )
        }
    }
}