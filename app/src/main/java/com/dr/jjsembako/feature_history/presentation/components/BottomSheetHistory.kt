package com.dr.jjsembako.feature_history.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetHistory(
    isFilterOn: MutableState<Boolean>,
    showSheet: MutableState<Boolean>,
    modifier: Modifier
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { showSheet.value = false },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        content = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.filter),
                    modifier = modifier.padding(bottom = 8.dp),
                    fontSize = 24.sp
                )
                Divider(color = MaterialTheme.colorScheme.tertiary, thickness = 1.dp)
                Spacer(modifier = modifier.height(8.dp))
                //TODO: Add filter date
                Spacer(modifier = modifier.height(32.dp))
                Button(
                    onClick = {
                        coroutineScope.launch {
                            modalBottomSheetState.hide()
                            showSheet.value = false
                        }
                    },
                    enabled = true,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(stringResource(R.string.close))
                }
                Spacer(modifier = modifier.height(32.dp))
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
private fun BottomSheetCustomerPreview() {
    JJSembakoTheme {
        BottomSheetHistory(
            isFilterOn = remember { mutableStateOf(false) },
            showSheet = remember { mutableStateOf(true) },
            modifier = Modifier
        )
    }
}