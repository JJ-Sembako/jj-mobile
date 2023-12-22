package com.dr.jjsembako.feature_warehouse.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun GudangScreen(onNavigateBack: () -> Unit, modifier: Modifier = Modifier) {
}

@Preview(showBackground = true)
@Composable
fun GudangScreenPreview() {
    JJSembakoTheme {
        GudangScreen(
            onNavigateBack = {}
        )
    }
}