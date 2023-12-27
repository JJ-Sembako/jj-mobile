package com.dr.jjsembako.feature_order.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun SelectPayment(
    selectedOption: FilterOption,
    onOptionSelected: (FilterOption) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        SelectPaymentHeader(modifier)
        SelectPaymentContent(selectedOption, onOptionSelected, modifier)
    }
}

@Composable
private fun SelectPaymentHeader(modifier: Modifier) {
    Divider(
        modifier = modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .width(1.dp), color = MaterialTheme.colorScheme.tertiary
    )
    Column(modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)){
        Text(
            text = stringResource(id = R.string.select_payment), fontSize = 14.sp
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
fun SelectPaymentContent(
    selectedOption: FilterOption,
    onOptionSelected: (FilterOption) -> Unit,
    modifier: Modifier
) {
    Spacer(modifier = modifier.height(16.dp))
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
    ) {
        items(items = paymentList, itemContent = { payment ->
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (payment == selectedOption),
                        onClick = { onOptionSelected(payment) }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (payment == selectedOption),
                    onClick = { onOptionSelected(payment) },
                    modifier = modifier.padding(all = Dp(value = 8F))
                )
                Text(text = payment.name, fontSize = 14.sp, modifier = modifier.padding(start = 8.dp))
            }
        })
    }
    Divider(
        modifier = modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .width(1.dp), color = MaterialTheme.colorScheme.tertiary
    )
}

private val paymentList = listOf(
    FilterOption("Tempo", "tempo"),
    FilterOption("Tunai", "tunai")
)

@Preview(showBackground = true)
@Composable
fun SelectPaymentPreview() {
    JJSembakoTheme {
        SelectPayment(
            selectedOption = FilterOption("Tempo", "tempo"),
            onOptionSelected = {},
            modifier = Modifier
        )
    }
}