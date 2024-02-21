package com.dr.jjsembako.feature_performance.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.theme.dialogMaxWidth
import com.dr.jjsembako.core.presentation.theme.dialogMinWidth
import kotlinx.coroutines.launch

@Composable
fun MonthYearPickerDialog(
    thisYear: Int,
    maxRange: Int,
    isTimeChange: MutableState<Boolean>,
    selectedYear: MutableState<Int>,
    selectedMonth: MutableState<Int>,
    showDialog: MutableState<Boolean>,
    modifier: Modifier
) {
    val tempSelectedYear = remember { mutableIntStateOf(selectedYear.value) }
    val tempSelectedMonth = remember { mutableIntStateOf(selectedMonth.value) }

    val yearRange = 2023..(thisYear + maxRange)
    val lazyListStateYear = rememberLazyListState()
    val lazyListStateMonth = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val month = listOf(
        stringResource(R.string.month_01), stringResource(R.string.month_02),
        stringResource(R.string.month_03), stringResource(R.string.month_04),
        stringResource(R.string.month_05), stringResource(R.string.month_06),
        stringResource(R.string.month_07), stringResource(R.string.month_08),
        stringResource(R.string.month_09), stringResource(R.string.month_10),
        stringResource(R.string.month_11), stringResource(R.string.month_12),
    )

    SideEffect {
        coroutineScope.launch {
            val indexOfSelectedYear = yearRange.indexOf(selectedYear.value)
            val indexOfSelectedMonth = selectedMonth.value
            lazyListStateYear.scrollToItem(indexOfSelectedYear)
            lazyListStateMonth.scrollToItem(indexOfSelectedMonth)
        }
    }

    Dialog(
        onDismissRequest = { showDialog.value = false }
    ) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .sizeIn(
                    minWidth = dialogMinWidth,
                    maxWidth = dialogMaxWidth
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.secondary)
                    .padding(16.dp), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.choose_time),
                    fontWeight = FontWeight.Normal, textAlign = TextAlign.Center,
                    fontSize = 10.sp, color = MaterialTheme.colorScheme.onPrimary,
                    modifier = modifier.wrapContentSize(Alignment.Center)
                )
                Text(
                    text = "${month[selectedMonth.value]} ${selectedYear.value}",
                    fontWeight = FontWeight.Medium, textAlign = TextAlign.Center,
                    fontSize = 32.sp, color = MaterialTheme.colorScheme.onPrimary,
                    modifier = modifier.wrapContentSize(Alignment.Center)
                )
            }
            Spacer(modifier = modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LazyColumn(
                    state = lazyListStateMonth,
                    modifier = Modifier.size(100.dp, 150.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(month) { index, monthItem ->
                        MonthPickerItem(monthItem, index == tempSelectedMonth.intValue) {
                            tempSelectedMonth.intValue = index
                        }

                    }
                }
                Spacer(modifier = modifier.width(16.dp))
                LazyColumn(
                    state = lazyListStateYear,
                    modifier = Modifier.size(100.dp, 150.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    yearRange.forEach { year ->
                        item {
                            YearPickerItem(year, year == tempSelectedYear.intValue) {
                                tempSelectedYear.intValue = year
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    showDialog.value = false
                    if(!isTimeChange.value) isTimeChange.value = true
                    selectedYear.value = tempSelectedYear.intValue
                    selectedMonth.value = tempSelectedMonth.intValue
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Red,
                    containerColor = Color.Red
                ),
                shape = RoundedCornerShape(50.dp),
                modifier = modifier.padding(horizontal = 5.dp, vertical = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.close),
                    color = Color.White, fontSize = 12.sp,
                    modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }
        }
    }
}

@Composable
private fun YearPickerItem(
    year: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(contentAlignment = Alignment.Center) {
        Box(
            Modifier
                .size(88.dp, 48.dp)
                .background(
                    if (selected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surface
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                year.toString(),
                fontSize = 18.sp,
                color = if (selected)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun MonthPickerItem(
    month: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(contentAlignment = Alignment.Center) {
        Box(
            Modifier
                .size(88.dp, 48.dp)
                .background(
                    if (selected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surface
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                month,
                fontSize = 18.sp,
                color = if (selected)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}