package com.dr.jjsembako.pesanan.presentation.components.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asLiveData
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.pesanan.presentation.create_order.create.BuatPesananViewModel
import kotlinx.coroutines.launch

@Composable
fun SelectPayment(
    buatPesananViewModel: BuatPesananViewModel,
    paymentList: List<FilterOption>,
    selectedOption: MutableState<String>,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        SelectPaymentHeader(modifier)
        SelectPaymentContent(
            buatPesananViewModel,
            paymentList,
            selectedOption,
            modifier
        )
    }
}

@Composable
private fun SelectPaymentHeader(modifier: Modifier) {
    Divider(
        modifier = modifier
            .fillMaxWidth()
            .width(1.dp), color = MaterialTheme.colorScheme.tertiary
    )
    Column(modifier = modifier.padding(horizontal = 8.dp, vertical = 16.dp)) {
        Text(
            text = stringResource(id = R.string.select_payment), fontSize = 14.sp
        )
    }
    Divider(
        modifier = modifier
            .fillMaxWidth()
            .width(1.dp), color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
private fun SelectPaymentContent(
    buatPesananViewModel: BuatPesananViewModel,
    paymentList: List<FilterOption>,
    selectedOption: MutableState<String>,
    modifier: Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val payment = buatPesananViewModel.payment.asLiveData().observeAsState().value
    val selectedCustomer = buatPesananViewModel.selectedCustomer.observeAsState().value

    LaunchedEffect(Unit) {
        if (payment != null && payment == paymentList[0].value) {
            selectedOption.value = paymentList[0].value
        } else if (payment != null && payment == paymentList[1].value) {
            selectedOption.value = paymentList[1].value
        }
        // set payment to tunai (PAID) if customer has debt
        if (selectedCustomer != null && selectedCustomer.debt != 0L) {
            buatPesananViewModel.setPayment(paymentList[1].value)
            selectedOption.value = paymentList[1].value
        }
    }
    LaunchedEffect(selectedOption.value) {
        // set payment to tunai (PAID) if customer has debt
        if (selectedCustomer != null && selectedCustomer.debt != 0L && selectedOption.value == paymentList[0].value) {
            buatPesananViewModel.setPayment(paymentList[1].value)
            selectedOption.value = paymentList[1].value
        }
    }
    LaunchedEffect(payment) {
        if (payment != null) selectedOption.value = payment
    }

    Spacer(modifier = modifier.height(16.dp))
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    ) {
        paymentList.forEach { paymentOpt ->
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .selectable(
                        // disable option TEMPO (PENDING) if customer data is not null & debt is not 0
                        enabled = !(selectedCustomer != null && selectedCustomer.debt != 0L && paymentOpt.name == paymentList[0].name),
                        selected = (paymentOpt.value == selectedOption.value),
                        onClick = {
                            if (selectedCustomer != null) {
                                coroutineScope.launch {
                                    if (paymentOpt.name == paymentList[0].name) {
                                        // if TEMPO (PENDING) is selected
                                        if (selectedCustomer.debt == 0L) {
                                            buatPesananViewModel.setPayment(paymentList[0].value)
                                        } else {
                                            buatPesananViewModel.setPayment(paymentList[1].value)
                                        }
                                    } else {
                                        // if TUNAI (PAID) is selected
                                        buatPesananViewModel.setPayment(paymentList[1].value)
                                    }
                                }
                            } else {
                                coroutineScope.launch {
                                    if (paymentOpt.name == paymentList[0].name) {
                                        buatPesananViewModel.setPayment(paymentList[0].value)
                                    } else buatPesananViewModel.setPayment(paymentList[1].value)
                                }
                            }
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    // disable option TEMPO (PENDING) if customer data is not null & debt is not 0
                    enabled = !(selectedCustomer != null && selectedCustomer.debt != 0L && paymentOpt.name == paymentList[0].name),
                    selected = (paymentOpt.value == selectedOption.value),
                    onClick = {
                        if (selectedCustomer != null) {
                            coroutineScope.launch {
                                if (paymentOpt.name == paymentList[0].name) {
                                    // if TEMPO (PENDING) is selected
                                    if (selectedCustomer.debt == 0L) {
                                        buatPesananViewModel.setPayment(paymentList[0].value)
                                    } else {
                                        buatPesananViewModel.setPayment(paymentList[1].value)
                                    }
                                } else {
                                    // if TUNAI (PAID) is selected
                                    buatPesananViewModel.setPayment(paymentList[1].value)
                                }
                            }
                        } else {
                            coroutineScope.launch {
                                if (paymentOpt.name == paymentList[0].name) {
                                    buatPesananViewModel.setPayment(paymentList[0].value)
                                } else buatPesananViewModel.setPayment(paymentList[1].value)
                            }
                        }
                    },
                    modifier = modifier.padding(all = Dp(value = 8F))
                )
                Text(
                    text = paymentOpt.name,
                    fontSize = 14.sp,
                    modifier = modifier.padding(start = 8.dp)
                )
            }
        }
    }
    Divider(
        modifier = modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .width(1.dp), color = MaterialTheme.colorScheme.tertiary
    )
}

@Preview(showBackground = true)
@Composable
private fun SelectPaymentPreview() {
    JJSembakoTheme {
        val payment = listOf(
            FilterOption("Tempo", "PENDING"),
            FilterOption("Tunai", "PAID")
        )
        SelectPayment(
            buatPesananViewModel = hiltViewModel(),
            paymentList = payment,
            selectedOption = remember { mutableStateOf(payment[0].value) },
            modifier = Modifier
        )
    }
}