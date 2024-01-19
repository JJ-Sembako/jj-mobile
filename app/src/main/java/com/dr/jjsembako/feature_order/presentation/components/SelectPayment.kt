package com.dr.jjsembako.feature_order.presentation.components

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
import com.dr.jjsembako.feature_order.presentation.create_order.BuatPesananViewModel
import kotlinx.coroutines.launch

@Composable
fun SelectPayment(
    buatPesananViewModel: BuatPesananViewModel,
    paymentList: List<FilterOption>,
    selectedOption: MutableState<FilterOption>,
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
    selectedOption: MutableState<FilterOption>,
    modifier: Modifier
) {
    val coroutineScope = rememberCoroutineScope()
//    val payment = buatPesananViewModel.payment.asLiveData().observeAsState().value
//    val selectedCustomer = buatPesananViewModel.selectedCustomer.observeAsState().value

//    LaunchedEffect(Unit) {
//        if (payment == true) {
//            selectedOption.value = paymentList[1]
//        } else if (payment == false) {
//            selectedOption.value = paymentList[0]
//        }
//        if (selectedCustomer != null && selectedCustomer.debt != 0L) {
//            buatPesananViewModel.setPayment(false)
//            selectedOption.value = paymentList[0]
//        }
//    }
//    LaunchedEffect(selectedOption.value) {
//        if (selectedCustomer != null && selectedCustomer.debt != 0L && selectedOption.value == paymentList[1]) {
//            buatPesananViewModel.setPayment(false)
//            selectedOption.value = paymentList[0]
//        }
//    }

    Spacer(modifier = modifier.height(16.dp))
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    ) {
//        paymentList.forEach { paymentOpt ->
//            Row(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .selectable(
//                        enabled = !(selectedCustomer != null && selectedCustomer.debt != 0L && paymentOpt.name == "Tempo"),
//                        selected = (paymentOpt == selectedOption.value),
//                        onClick = {
//                            if (selectedCustomer != null) {
//                                coroutineScope.launch {
//                                    if (paymentOpt.name == "Tempo") {
//                                        if (selectedCustomer.debt != 0L) {
//                                            buatPesananViewModel.setPayment(true)
//                                            selectedOption.value = paymentOpt
//                                        }
//                                    } else {
//                                        buatPesananViewModel.setPayment(false)
//                                        selectedOption.value = paymentOpt
//                                    }
//                                }
//                            } else {
//                                coroutineScope.launch {
//                                    if (paymentOpt.name == "Tempo") buatPesananViewModel.setPayment(
//                                        true
//                                    )
//                                    else buatPesananViewModel.setPayment(false)
//                                }
//                                selectedOption.value = paymentOpt
//                            }
//                        }
//                    ),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                RadioButton(
//                    enabled = !(selectedCustomer != null && selectedCustomer.debt != 0L && paymentOpt.name == "Tempo"),
//                    selected = (paymentOpt == selectedOption.value),
//                    onClick = {
//                        if (selectedCustomer != null) {
//                            coroutineScope.launch {
//                                if (paymentOpt.name == "Tempo") {
//                                    if (selectedCustomer.debt != 0L) {
//                                        buatPesananViewModel.setPayment(true)
//                                        selectedOption.value = paymentOpt
//                                    }
//                                } else {
//                                    buatPesananViewModel.setPayment(false)
//                                    selectedOption.value = paymentOpt
//                                }
//                            }
//                        } else {
//                            coroutineScope.launch {
//                                if (paymentOpt.name == "Tempo") buatPesananViewModel.setPayment(true)
//                                else buatPesananViewModel.setPayment(false)
//                            }
//                            selectedOption.value = paymentOpt
//                        }
//                    },
//                    modifier = modifier.padding(all = Dp(value = 8F))
//                )
//                Text(
//                    text = paymentOpt.name,
//                    fontSize = 14.sp,
//                    modifier = modifier.padding(start = 8.dp)
//                )
//            }
//        }
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
        SelectPayment(
            buatPesananViewModel = hiltViewModel(),
            paymentList = listOf(
                FilterOption("Tempo", "tempo"),
                FilterOption("Tunai", "tunai")
            ),
            selectedOption = remember { mutableStateOf(FilterOption("Tempo", "tempo")) },
            modifier = Modifier
        )
    }
}