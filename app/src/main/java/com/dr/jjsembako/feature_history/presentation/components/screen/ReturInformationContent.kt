package com.dr.jjsembako.feature_history.presentation.components.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dr.jjsembako.core.data.remote.response.order.ReturData
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_history.presentation.components.item_list.ReturItem

@Composable
fun ReturInformationContent(
    dataRetur: List<ReturData?>?,
    showDialogRetur: MutableState<Boolean>,
    showDialogPreview: MutableState<Boolean>,
    previewProductName: MutableState<String>,
    previewProductImage: MutableState<String>,
    idDeleteRetur: MutableState<String>,
    statusRetur: MutableState<Int>,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (dataRetur.isNullOrEmpty()) {
            Spacer(modifier = modifier.height(48.dp))
        } else {
            dataRetur.forEach { item ->
                if (item != null) {
                    key(item.id) {
                        ReturItem(
                            item, showDialogRetur, showDialogPreview, previewProductName,
                            previewProductImage, idDeleteRetur, statusRetur, modifier
                        )
                        Spacer(modifier = modifier.height(8.dp))
                    }
                }
            }
        }
    }
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
                dataRetur = null,
                showDialogRetur = remember { mutableStateOf(true) },
                showDialogPreview = remember { mutableStateOf(true) },
                previewProductName = remember { mutableStateOf("") },
                previewProductImage = remember { mutableStateOf("") },
                idDeleteRetur = remember { mutableStateOf("") },
                statusRetur = remember { mutableIntStateOf(0) },
                modifier = Modifier
            )
        }
    }
}