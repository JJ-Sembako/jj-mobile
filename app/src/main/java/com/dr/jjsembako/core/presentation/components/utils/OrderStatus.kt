package com.dr.jjsembako.core.presentation.components.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun OrderStatus(
    status: Int,
    modifier: Modifier
) {
}

@Composable
@Preview(showBackground = true)
private fun OrderStatusPreview() {
    JJSembakoTheme {
        OrderStatus(
            status = 1,
            modifier = Modifier
        )
    }
}