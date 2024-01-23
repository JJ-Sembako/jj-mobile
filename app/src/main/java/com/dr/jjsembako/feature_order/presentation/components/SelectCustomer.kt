package com.dr.jjsembako.feature_order.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.core.presentation.components.card.CustomerInfo
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun SelectCustomer(
    customer: DataCustomer?,
    onSelectCustomer: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        SelectCustomerHeader(onSelectCustomer, modifier)
        SelectCustomerContent(customer, modifier)
    }
}

@Composable
private fun SelectCustomerHeader(onSelectCustomer: () -> Unit, modifier: Modifier) {
    Divider(
        modifier = modifier
            .fillMaxWidth()
            .width(1.dp), color = MaterialTheme.colorScheme.tertiary
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelectCustomer() }
            .padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = stringResource(id = R.string.select_cust), fontSize = 14.sp)
        Icon(
            Icons.Default.KeyboardArrowRight,
            contentDescription = stringResource(R.string.select_cust)
        )
    }
    Divider(
        modifier = modifier
            .fillMaxWidth()
            .width(1.dp), color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
private fun SelectCustomerContent(customer: DataCustomer?, modifier: Modifier) {
    if (customer != null) {
        Spacer(modifier = modifier.height(16.dp))
        CustomerInfo(onNavigateToDetailCust = {}, customer = customer, modifier = modifier)
        Divider(
            modifier = modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .width(1.dp), color = MaterialTheme.colorScheme.tertiary
        )
    } else {
        Spacer(modifier = modifier.height(128.dp))
        Divider(
            modifier = modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .width(1.dp), color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectCustomer1Preview() {
    JJSembakoTheme {
        SelectCustomer(
            customer = null,
            onSelectCustomer = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectCustomer2Preview() {
    JJSembakoTheme {
        SelectCustomer(
            customer = DataCustomer(
                "abcd-123",
                "Bambang",
                "Toko Makmur",
                "Jl. Nusa Indah 3, Belimbing, Jambu, Sayuran, Tumbuhan",
                "https:://maps.app.goo.gl/MQBEcptYvdYfBaNc9",
                "081234567890",
                1_500_000L
            ),
            onSelectCustomer = {},
            modifier = Modifier
        )
    }
}