package com.dr.jjsembako.feature_history.presentation.components

import android.os.Build
import android.util.Log
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.convertMillisToDate
import com.dr.jjsembako.core.utils.initializeDateValues
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetHistory(
    fromDate: MutableState<String>,
    untilDate: MutableState<String>,
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
                SwitchFilter(
                    fromDate = fromDate,
                    untilDate = untilDate,
                    isFilterOn = isFilterOn,
                    modifier = modifier
                )
                DateFilter(
                    fromDate = fromDate,
                    untilDate = untilDate,
                    isFilterOn = isFilterOn,
                    modifier = modifier
                )
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
private fun SwitchFilter(
    fromDate: MutableState<String>,
    untilDate: MutableState<String>,
    isFilterOn: MutableState<Boolean>,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(id = R.string.activate_filter))
        Spacer(modifier = modifier.width(8.dp))
        Switch(checked = isFilterOn.value, onCheckedChange = {
            isFilterOn.value = it
            if (isFilterOn.value) {
                initializeDateValues(fromDate, untilDate)
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateFilter(
    fromDate: MutableState<String>,
    untilDate: MutableState<String>,
    isFilterOn: MutableState<Boolean>,
    modifier: Modifier
) {
    val calendarFromDate = Calendar.getInstance()
    val calendarUntilDate = Calendar.getInstance()

    // convert date string to calendar
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val fromDateLocalDate =
                LocalDate.parse(fromDate.value, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            val untilDateLocalDate =
                LocalDate.parse(untilDate.value, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            calendarFromDate.time =
                Date.from(fromDateLocalDate.atStartOfDay(ZoneId.of("UTC")).toInstant())
            calendarUntilDate.time =
                Date.from(untilDateLocalDate.atStartOfDay(ZoneId.of("UTC")).toInstant())
            Log.e("Calendar From Date", "Calendar From Date: ${calendarFromDate.time}")
        } else {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            dateFormat.isLenient = false
            calendarFromDate.time = dateFormat.parse(fromDate.value)!!
            calendarUntilDate.time = dateFormat.parse(untilDate.value)!!
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    // set the initial date
    val datePickerStateFromDate =
        rememberDatePickerState(initialSelectedDateMillis = calendarFromDate.timeInMillis)
    val datePickerStateUntilDate =
        rememberDatePickerState(initialSelectedDateMillis = calendarUntilDate.timeInMillis)

    var showDatePickerFromDate by remember { mutableStateOf(false) }
    var showDatePickerUntilDate by remember { mutableStateOf(false) }

    val interactionSourceFromDate = remember {
        object : MutableInteractionSource {
            override val interactions = MutableSharedFlow<Interaction>(
                extraBufferCapacity = 16,
                onBufferOverflow = BufferOverflow.DROP_OLDEST,
            )

            override suspend fun emit(interaction: Interaction) {
                if (interaction is PressInteraction.Release) {
                    showDatePickerUntilDate = false
                    showDatePickerFromDate = true
                }

                interactions.emit(interaction)
            }

            override fun tryEmit(interaction: Interaction): Boolean {
                return interactions.tryEmit(interaction)
            }
        }
    }
    val interactionSourceUntilDate = remember {
        object : MutableInteractionSource {
            override val interactions = MutableSharedFlow<Interaction>(
                extraBufferCapacity = 16,
                onBufferOverflow = BufferOverflow.DROP_OLDEST,
            )

            override suspend fun emit(interaction: Interaction) {
                if (interaction is PressInteraction.Release) {
                    showDatePickerFromDate = false
                    showDatePickerUntilDate = true
                }

                interactions.emit(interaction)
            }

            override fun tryEmit(interaction: Interaction): Boolean {
                return interactions.tryEmit(interaction)
            }
        }
    }
    if (isFilterOn.value) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = fromDate.value,
                readOnly = true,
                label = { Text(stringResource(R.string.from_date)) },
                onValueChange = { fromDate.value = it },
                singleLine = true,
                interactionSource = interactionSourceFromDate,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            )
            OutlinedTextField(
                value = untilDate.value,
                readOnly = true,
                label = { Text(stringResource(R.string.until_date)) },
                onValueChange = { untilDate.value = it },
                singleLine = true,
                interactionSource = interactionSourceUntilDate,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            )
        }
        if (showDatePickerFromDate) {
            DatePickerDialog(
                onDismissRequest = {
                    showDatePickerFromDate = false
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDatePickerFromDate = false
                        fromDate.value = datePickerStateFromDate.selectedDateMillis?.let {
                            convertMillisToDate(it)
                        }.toString()
                    }) {
                        Text(text = stringResource(id = R.string.save))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDatePickerFromDate = false
                    }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                }
            ) {
                DatePicker(
                    state = datePickerStateFromDate,
                )
            }
        }
        if (showDatePickerUntilDate) {
            DatePickerDialog(
                onDismissRequest = {
                    showDatePickerUntilDate = false
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDatePickerUntilDate = false
                        untilDate.value = datePickerStateUntilDate.selectedDateMillis?.let {
                            convertMillisToDate(it)
                        }.toString()
                    }) {
                        Text(text = stringResource(id = R.string.save))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDatePickerUntilDate = false
                    }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                }
            ) {
                DatePicker(
                    state = datePickerStateUntilDate
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun BottomSheetCustomerPreview() {
    JJSembakoTheme {
        BottomSheetHistory(
            fromDate = remember { mutableStateOf("10-12-2023") },
            untilDate = remember { mutableStateOf("12-12-2023") },
            isFilterOn = remember { mutableStateOf(false) },
            showSheet = remember { mutableStateOf(true) },
            modifier = Modifier
        )
    }
}