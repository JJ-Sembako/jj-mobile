package com.dr.jjsembako.feature_history.presentation.components.retur

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dr.jjsembako.core.data.model.SelectPNRItem
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_history.presentation.components.PNRSelectProductHeader
import com.dr.jjsembako.feature_history.presentation.components.card.SelectedOrderRCard
import com.dr.jjsembako.feature_history.presentation.retur.create.ReturViewModel

@Composable
fun RSelectedProduct(
    data: SelectPNRItem? = null,
    viewModel: ReturViewModel,
    showDialog: MutableState<Boolean>,
    previewProductName: MutableState<String>,
    previewProductImage: MutableState<String>,
    onSelectProduct: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        PNRSelectProductHeader(onSelectProduct = { onSelectProduct() }, modifier = modifier)
        RSelectedProductContent(
            data = data, viewModel = viewModel, showDialog = showDialog,
            previewProductName = previewProductName, previewProductImage = previewProductImage,
            modifier = modifier
        )
    }
}

@Composable
private fun RSelectedProductContent(
    data: SelectPNRItem? = null,
    viewModel: ReturViewModel,
    showDialog: MutableState<Boolean>,
    previewProductName: MutableState<String>,
    previewProductImage: MutableState<String>,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (data == null) Spacer(modifier = modifier.height(128.dp))
        else {
            Spacer(modifier = modifier.height(8.dp))
            SelectedOrderRCard(
                viewModel, data, showDialog,
                previewProductName, previewProductImage, modifier
            )
            Spacer(modifier = modifier.height(8.dp))
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
private fun RSelectedProductPreview() {
    JJSembakoTheme {
        val viewModel: ReturViewModel = hiltViewModel()
        RSelectedProduct(
            viewModel = viewModel,
            showDialog = remember { mutableStateOf(true) },
            previewProductName = remember { mutableStateOf("") },
            previewProductImage = remember { mutableStateOf("") },
            onSelectProduct = {},
            modifier = Modifier
        )
    }
}