package com.dr.jjsembako.ui.feature_customer.edit

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dr.jjsembako.ui.theme.JJSembakoTheme

@Composable
fun EditPelangganScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetailCust: () -> Unit,
    modifier: Modifier = Modifier
) {
}

@Composable
@Preview(showBackground = true)
fun EditPelangganScreenPreview() {
    JJSembakoTheme {
        EditPelangganScreen(onNavigateBack = {}, onNavigateToDetailCust = {})
    }
}