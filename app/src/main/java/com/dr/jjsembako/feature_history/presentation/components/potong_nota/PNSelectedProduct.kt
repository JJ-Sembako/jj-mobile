package com.dr.jjsembako.feature_history.presentation.components.potong_nota

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_history.presentation.components.PNRSelectProductHeader

@Composable
fun PNSelectedProduct(
    onSelectProduct: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        PNRSelectProductHeader(onSelectProduct = { onSelectProduct() }, modifier = modifier)
        PNSelectedProductContent(modifier = modifier)
    }
}

@Composable
private fun PNSelectedProductContent(
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = modifier.height(128.dp))
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
private fun PNSelectedProductPreview() {
    JJSembakoTheme {
        PNSelectedProduct(
            onSelectProduct = {},
            modifier = Modifier
        )
    }
}