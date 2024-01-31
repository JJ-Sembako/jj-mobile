package com.dr.jjsembako.feature_history.presentation.add_product_order

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun CatalogContentAdd(
    id: String,
    modifier: Modifier
) {
}

@Preview(showBackground = true)
@Composable
private fun CatalogContentAddPreview() {
    JJSembakoTheme {
        CatalogContentAdd(
            id = "123",
            modifier = Modifier
        )
    }
}