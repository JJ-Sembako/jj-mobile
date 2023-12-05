package com.dr.jjsembako.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.dr.jjsembako.R
import com.dr.jjsembako.core.model.FilterOption
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetOrder(
    optionListPayment: List<FilterOption>,
    optionListOrder: List<FilterOption>,
    selectedOptionPayment: FilterOption,
    selectedOptionOrder: FilterOption,
    onOptionSelectedPayment: (FilterOption) -> Unit,
    onOptionSelectedOrder: (FilterOption) -> Unit,
    showSheet: MutableState<Boolean>,
    modifier: Modifier
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { showSheet.value = false },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        content = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .heightIn(min = 400.dp, max = 600.dp)
                ) {
                    Text(
                        text = stringResource(R.string.payment_status),
                        modifier = modifier.padding(bottom = 8.dp),
                        fontSize = 16.sp
                    )
                    Divider(color = MaterialTheme.colorScheme.tertiary, thickness = 1.dp)
                    Spacer(modifier = modifier.height(8.dp))
                    LazyColumn(
                        modifier = modifier
                            .fillMaxWidth(),
                    ) {
                        items(items = optionListPayment, itemContent = { filter ->
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (filter == selectedOptionPayment),
                                        onClick = { onOptionSelectedPayment(filter) }
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (filter == selectedOptionPayment),
                                    onClick = { onOptionSelectedPayment(filter) },
                                    modifier = modifier.padding(all = Dp(value = 8F))
                                )
                                Text(text = filter.name, modifier = modifier.padding(start = 8.dp))
                            }
                        })
                    }
                    Spacer(modifier = modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.order_status),
                        modifier = modifier.padding(bottom = 8.dp),
                        fontSize = 16.sp
                    )
                    Divider(color = MaterialTheme.colorScheme.tertiary, thickness = 1.dp)
                    Spacer(modifier = modifier.height(8.dp))
                    LazyColumn(
                        modifier = modifier
                            .fillMaxWidth(),
                    ) {
                        items(items = optionListOrder, itemContent = { filter ->
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (filter == selectedOptionOrder),
                                        onClick = { onOptionSelectedOrder(filter) }
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (filter == selectedOptionOrder),
                                    onClick = { onOptionSelectedOrder(filter) },
                                    modifier = modifier.padding(all = Dp(value = 8F))
                                )
                                Text(text = filter.name, modifier = modifier.padding(start = 8.dp))
                            }
                        })
                    }
                }
                Spacer(modifier = modifier.height(32.dp))
                Button(
                    onClick = {
                        coroutineScope.launch {
                            modalBottomSheetState.hide()
                            showSheet.value = false
                        }
                    },
                    enabled = true,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(stringResource(R.string.change))
                }
                Spacer(modifier = modifier.height(32.dp))
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
fun BottomSheetOrderPreview() {
    JJSembakoTheme {
        BottomSheetOrder(
            optionListPayment = listOf(
                FilterOption("Semua", "semua"),
                FilterOption("Belum Lunas", "belum-lunas"),
                FilterOption("Lunas", "lunas")
            ),
            optionListOrder = listOf(
                FilterOption("Semua", "semua"),
                FilterOption("Menunggu Konfirmasi", "menunggu-konfirmasi"),
                FilterOption("Dikemas", "dikemas"),
                FilterOption("Dikirim", "dikirim"),
                FilterOption("Selesai", "selesai")
            ),
            selectedOptionPayment = FilterOption("Semua", "semua"),
            selectedOptionOrder = FilterOption("Semua", "semua"),
            onOptionSelectedPayment = {},
            onOptionSelectedOrder = {},
            showSheet = remember { mutableStateOf(true) },
            modifier = Modifier
        )
    }
}