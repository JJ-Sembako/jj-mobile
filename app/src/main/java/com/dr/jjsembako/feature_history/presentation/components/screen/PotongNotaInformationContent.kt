package com.dr.jjsembako.feature_history.presentation.components.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_history.presentation.components.item_list.PotongNotaItem

@Composable
fun PotongNotaInformationContent(
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        PotongNotaItem(modifier)
        Spacer(modifier = modifier.height(8.dp))
        PotongNotaItem(modifier)
        Spacer(modifier = modifier.height(8.dp))
        PotongNotaItem(modifier)
        Spacer(modifier = modifier.height(8.dp))
        PotongNotaItem(modifier)
        Spacer(modifier = modifier.height(8.dp))
    }
}

@Composable
@Preview(showBackground = true)
private fun PotongNotaInformationContentPreview() {
    JJSembakoTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PotongNotaInformationContent(
                modifier = Modifier
            )
        }
    }
}