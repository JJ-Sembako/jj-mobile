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
import com.dr.jjsembako.core.presentation.theme.PaymentNotPaidOffBg
import com.dr.jjsembako.core.presentation.theme.PaymentNotPaidOffText
import com.dr.jjsembako.core.presentation.theme.PaymentPaidOffBg
import com.dr.jjsembako.core.presentation.theme.PaymentPaidOffText
import com.dr.jjsembako.core.presentation.theme.StatusNotFoundBg
import com.dr.jjsembako.core.presentation.theme.StatusNotFoundText

@Composable
fun PaymentStatus(
    status: Int,
    modifier: Modifier
) {

    val statusText = when (status) {
        0 -> stringResource(R.string.payment_not_paid_off)
        1 -> stringResource(R.string.payment_paid_off)
        else -> stringResource(R.string.status_not_found)
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(
                when (status) {
                    0 -> PaymentNotPaidOffBg
                    1 -> PaymentPaidOffBg
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
                    0 -> PaymentNotPaidOffText
                    1 -> PaymentPaidOffText
                    else -> StatusNotFoundText
                }
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PaymentStatusPreview() {
    JJSembakoTheme {
        Column(
            modifier = Modifier.size(150.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PaymentStatus(
                status = 0,
                modifier = Modifier
            )
        }
    }
}