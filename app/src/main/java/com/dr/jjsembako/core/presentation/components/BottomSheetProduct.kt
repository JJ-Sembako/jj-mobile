package com.dr.jjsembako.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetProduct(
    optionList: List<FilterOption?>?,
    checkBoxResult: MutableList<String>,
    checkBoxStates: MutableMap<String, Boolean>,
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
                    .heightIn(max = 640.dp)
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

                if (!optionList.isNullOrEmpty()) {
                    LazyColumn(
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(1f),
                    ) {
                        items(items = optionList, itemContent = { filter ->
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .toggleable(
                                        value = checkBoxStates.getValue(filter!!.value),
                                        onValueChange = {
                                            checkBoxStates[filter.value] = it
                                            if (it) checkBoxResult.add(filter.value)
                                            else checkBoxResult.remove(filter.value)
                                            if (checkBoxResult.isEmpty()) {
                                                checkBoxResult.addAll(optionList.map { it2 -> it2!!.value })
                                                checkBoxStates.putAll(optionList.map { it2 -> it2!!.value to true })
                                            }
                                        }
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = checkBoxStates.getValue(filter.value),
                                    onCheckedChange = {
                                        checkBoxStates[filter.value] = it
                                        if (it) checkBoxResult.add(filter.value)
                                        else checkBoxResult.remove(filter.value)
                                        if (checkBoxResult.isEmpty()) {
                                            checkBoxResult.addAll(optionList.map { it2 -> it2!!.value })
                                            checkBoxStates.putAll(optionList.map { it2 -> it2!!.value to true })
                                        }
                                    },
                                    modifier = modifier.padding(all = Dp(value = 8F))
                                )
                                Text(text = filter.name, modifier = modifier.padding(start = 8.dp))
                            }
                        })
                    }
                } else {
                    Text(text = stringResource(R.string.option_not_available))
                }

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
private fun BottomSheetProductPreview() {
    JJSembakoTheme {
        val option = listOf(
            FilterOption("Beras", "beras"),
            FilterOption("Minyak", "minyak"),
            FilterOption("Gula", "gula")
        )
        val checkBoxResult = remember { mutableStateListOf<String>() }
        val checkBoxStates = remember { mutableStateMapOf<String, Boolean>() }
        checkBoxResult.addAll(option.map { it.value })
        checkBoxStates.putAll(option.map { it.value to true })

        BottomSheetProduct(
            optionList = option,
            checkBoxResult = checkBoxResult,
            checkBoxStates = checkBoxStates,
            showSheet = remember { mutableStateOf(true) },
            modifier = Modifier
        )
    }
}