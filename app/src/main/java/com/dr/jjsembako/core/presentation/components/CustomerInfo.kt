package com.dr.jjsembako.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.formatRupiah

@Composable
fun CustomerInfo(
    onNavigateToDetailCust: (String) -> Unit,
    customer: DataCustomer,
    modifier: Modifier
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp)
            .clickable { onNavigateToDetailCust(customer.id) },
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
        ) {
            Text(
                text = customer.shopName, fontWeight = FontWeight.Bold, fontSize = 14.sp
            )
            Text(
                text = customer.name, fontSize = 12.sp
            )
            Text(
                text = customer.phoneNumber, fontSize = 12.sp
            )
            Text(
                text = customer.address, fontSize = 12.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Text(
                text = stringResource(R.string.total_debt, formatRupiah(customer.debt ?: 0)),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CustomerInfoPreview() {
    JJSembakoTheme {
        CustomerInfo(
            onNavigateToDetailCust = {},
            DataCustomer(
                "abcd-123",
                "Bambang",
                "Toko Makmur",
                "Jl. Nusa Indah 3, Belimbing, Jambu, Sayuran, Tumbuhan",
                "https://gmaps.com/123123",
                "081234567890",
                1_500_000L
            ), modifier = Modifier
        )
    }
}