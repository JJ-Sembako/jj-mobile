package com.dr.jjsembako.feature_order.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_order.presentation.create_order.BuatPesananViewModel

@Composable
fun SelectProduct(
    buatPesananViewModel: BuatPesananViewModel,
    onSelectProduct: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        SelectProductHeader(onSelectProduct, modifier)
        SelectProductContent(buatPesananViewModel, modifier)
    }
}

@Composable
private fun SelectProductHeader(onSelectProduct: () -> Unit, modifier: Modifier) {
    Divider(
        modifier = modifier
            .fillMaxWidth()
            .width(1.dp), color = MaterialTheme.colorScheme.tertiary
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelectProduct() }
            .padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = stringResource(id = R.string.select_product), fontSize = 14.sp)
        Icon(
            Icons.Default.KeyboardArrowRight,
            contentDescription = stringResource(R.string.select_product)
        )
    }
    Divider(
        modifier = modifier
            .fillMaxWidth()
            .width(1.dp), color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
private fun SelectProductContent(
    buatPesananViewModel: BuatPesananViewModel,
    modifier: Modifier
) {
    val dataProducts = buatPesananViewModel.dataProducts.observeAsState().value
    val loadingState = buatPesananViewModel.loadingState.observeAsState().value

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (loadingState == true) {
            Spacer(modifier = modifier.height(32.dp))
            CircularProgressIndicator(
                modifier = modifier.size(64.dp)
            )
            Spacer(modifier = modifier.height(32.dp))

        } else {
            if (dataProducts.isNullOrEmpty()) {
                Spacer(modifier = modifier.height(128.dp))
            } else {
                val filteredProducts = dataProducts.filter { product ->
                    product!!.isChosen
                }

                if (filteredProducts.isNotEmpty()) {
                    Spacer(modifier = modifier.height(16.dp))
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                    ) {
                        filteredProducts.forEach { product ->
                            key(product?.id ?: "empty-${System.currentTimeMillis()}") {
                                if (product != null) {
                                    ProductOnSelected(buatPesananViewModel, product, modifier)
                                }
                                Spacer(modifier = modifier.height(8.dp))
                            }
                        }
                    }
                    Spacer(modifier = modifier.height(8.dp))
                } else {
                    Spacer(modifier = modifier.height(128.dp))
                }
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
private fun SelectProductPreview() {
    JJSembakoTheme {
        SelectProduct(
            buatPesananViewModel = hiltViewModel(),
            onSelectProduct = {},
            modifier = Modifier
        )
    }
}