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
import com.dr.jjsembako.core.presentation.theme.StatusConfirmedReturBg
import com.dr.jjsembako.core.presentation.theme.StatusConfirmedReturText
import com.dr.jjsembako.core.presentation.theme.StatusDeliveredBg
import com.dr.jjsembako.core.presentation.theme.StatusDeliveredText
import com.dr.jjsembako.core.presentation.theme.StatusNotFoundBg
import com.dr.jjsembako.core.presentation.theme.StatusNotFoundText
import com.dr.jjsembako.core.presentation.theme.StatusWaitConfirmBg
import com.dr.jjsembako.core.presentation.theme.StatusWaitConfirmText

@Composable
fun ReturStatus(
    status: Int,
    modifier: Modifier
) {
    val statusText = when (status) {
        0 -> stringResource(R.string.status_wait_confirm)
        1 -> stringResource(R.string.status_confirmed)
        2 -> stringResource(R.string.status_delivered)
        else -> stringResource(R.string.status_not_found)
    }
    val colorBg = when (status) {
        0 -> StatusWaitConfirmBg
        1 -> StatusConfirmedReturBg
        2 -> StatusDeliveredBg
        else -> StatusNotFoundBg
    }
    val colorText = when (status) {
        0 -> StatusWaitConfirmText
        1 -> StatusConfirmedReturText
        2 -> StatusDeliveredText
        else -> StatusNotFoundText
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(colorBg)
            .padding(4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = statusText,
            fontWeight = FontWeight.Bold, fontSize = 12.sp,
            textAlign = TextAlign.Center,
            style = TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false), color = colorText
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ReturStatusPreview() {
    JJSembakoTheme {
        Column(
            modifier = Modifier.size(150.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReturStatus(
                status = 0,
                modifier = Modifier
            )
        }
    }
}