package com.dr.jjsembako.feature_history.presentation.edit_product_order

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun CartContentEdit(
    id: String,
    modifier: Modifier
) {
}

@Preview(showBackground = true)
@Composable
private fun CartContentEditPreview() {
    JJSembakoTheme {
        CartContentEdit(
            id = "123",
            modifier = Modifier
        )
    }
}