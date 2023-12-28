package com.dr.jjsembako.feature_order.presentation.select_product

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_warehouse.presentation.GudangScreen

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PilihBarangScreen(onNavigateBack: () -> Unit, modifier: Modifier = Modifier) {
}

@Preview(showBackground = true)
@Composable
private fun PilihBarangPreview() {
    JJSembakoTheme {
        PilihBarangScreen(
            onNavigateBack = {}
        )
    }
}