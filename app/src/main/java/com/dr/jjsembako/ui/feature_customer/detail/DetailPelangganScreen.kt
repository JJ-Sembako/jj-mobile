package com.dr.jjsembako.ui.feature_customer.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dr.jjsembako.ui.theme.JJSembakoTheme

@Composable
fun DetailPelangganScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEditCust: () -> Unit,
    modifier: Modifier = Modifier
) {
}

@Composable
@Preview(showBackground = true)
fun DetailPelangganScreenPreview() {
    JJSembakoTheme {
        DetailPelangganScreen(onNavigateBack = {}, onNavigateToEditCust = {})
    }
}