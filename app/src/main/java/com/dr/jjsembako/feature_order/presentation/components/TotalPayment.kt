package com.dr.jjsembako.feature_order.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.formatRupiah

@Composable
fun TotalPayment(totalPrice: Long = 0, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        TotalPaymentHeader(modifier)
        TotalPaymentContent(totalPrice, modifier)
    }
}

@Composable
fun TotalPaymentHeader(modifier: Modifier) {
    Divider(
        modifier = modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .width(1.dp), color = MaterialTheme.colorScheme.tertiary
    )
    Column(modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
        Text(
            text = stringResource(id = R.string.total_payment), fontSize = 14.sp
        )
    }
    Divider(
        modifier = modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .width(1.dp), color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
fun TotalPaymentContent(totalPrice: Long = 0, modifier: Modifier) {
    Spacer(modifier = modifier.height(32.dp))
    Column(modifier = modifier.padding(start = 16.dp)) {
        Text(
            text = formatRupiah(totalPrice), fontSize = 14.sp, fontWeight = FontWeight.Bold
        )
    }
    Spacer(modifier = modifier.height(32.dp))
    Divider(
        modifier = modifier
            .fillMaxWidth()
            .width(1.dp), color = MaterialTheme.colorScheme.tertiary
    )
}

@Preview(showBackground = true)
@Composable
fun TotalPaymentPreview() {
    JJSembakoTheme {
        TotalPayment(totalPrice = 175500, modifier = Modifier)
    }
}