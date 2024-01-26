package com.dr.jjsembako.feature_history.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun ReturInformationContent(
    modifier: Modifier
) {
}

@Composable
@Preview(showBackground = true)
private fun ReturInformationContentPreview() {
    JJSembakoTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReturInformationContent(
                modifier = Modifier
            )
        }
    }
}