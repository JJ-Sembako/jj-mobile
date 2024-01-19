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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    val loadingState = buatPesananViewModel.loadingState.observeAsState().value

    if (loadingState == true) {
        Spacer(modifier = modifier.height(16.dp))
        CircularProgressIndicator(
            modifier = modifier.size(48.dp)
        )
        Spacer(modifier = modifier.height(16.dp))
        Text(
            text = stringResource(R.string.on_progress),
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = modifier.wrapContentSize(Alignment.Center)
        )
        Spacer(modifier = modifier.height(16.dp))
        Text(
            text = stringResource(R.string.please_wait),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = modifier.wrapContentSize(Alignment.Center)
        )
        Spacer(modifier = modifier.height(16.dp))
    } else {
        Spacer(modifier = modifier.height(64.dp))
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