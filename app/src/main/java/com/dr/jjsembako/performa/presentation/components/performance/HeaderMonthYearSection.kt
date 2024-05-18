package com.dr.jjsembako.performa.presentation.components.performance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R

@Composable
fun HeaderMonthYearSection(
    thisYear: Int,
    maxRange: Int,
    isTimeChange: MutableState<Boolean>,
    selectedYear: MutableState<Int>,
    selectedMonth: MutableState<Int>,
    showDialog: MutableState<Boolean>,
    modifier: Modifier
) {
    val month = listOf(
        stringResource(R.string.month_01), stringResource(R.string.month_02),
        stringResource(R.string.month_03), stringResource(R.string.month_04),
        stringResource(R.string.month_05), stringResource(R.string.month_06),
        stringResource(R.string.month_07), stringResource(R.string.month_08),
        stringResource(R.string.month_09), stringResource(R.string.month_10),
        stringResource(R.string.month_11), stringResource(R.string.month_12),
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            enabled = !(selectedYear.value == 2023 && selectedMonth.value == 0),
            onClick = {
                if(!isTimeChange.value) isTimeChange.value = true
                if (selectedMonth.value == 0) {
                    selectedYear.value = selectedYear.value - 1
                    selectedMonth.value = 11
                } else {
                    selectedMonth.value = selectedMonth.value - 1
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowCircleLeft,
                contentDescription = stringResource(R.string.min_time),
                tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.tertiary,
                modifier = modifier.size(32.dp)
            )
        }
        Spacer(modifier = modifier.width(32.dp))
        Text(
            text = "${month[selectedMonth.value]} ${selectedYear.value}", fontWeight = FontWeight.Bold,
            fontSize = 18.sp, textAlign = TextAlign.Center,
            modifier = modifier
                .wrapContentSize(Alignment.Center)
                .clickable { showDialog.value = true }
        )
        Spacer(modifier = modifier.width(32.dp))
        IconButton(
            enabled = !(selectedYear.value == (thisYear + maxRange) && selectedMonth.value == 11),
            onClick = {
                if(!isTimeChange.value) isTimeChange.value = true
                if (selectedMonth.value == 11){
                    selectedYear.value = selectedYear.value + 1
                    selectedMonth.value = 0
                } else {
                    selectedMonth.value = selectedMonth.value + 1                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowCircleRight,
                contentDescription = stringResource(R.string.add_time),
                tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.tertiary,
                modifier = modifier.size(32.dp)
            )
        }
    }
}