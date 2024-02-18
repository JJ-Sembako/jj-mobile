package com.dr.jjsembako.feature_performance.presentation.components

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
fun HeaderYearSection(
    thisYear: Int,
    maxRange: Int,
    selectedYear: MutableState<Int>,
    showDialog: MutableState<Boolean>,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            enabled = selectedYear.value != 2023,
            onClick = { selectedYear.value = selectedYear.value - 1 }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowCircleLeft,
                contentDescription = stringResource(R.string.min_year),
                tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.tertiary,
                modifier = modifier.size(32.dp)
            )
        }
        Spacer(modifier = modifier.width(32.dp))
        Text(
            text = selectedYear.value.toString(), fontWeight = FontWeight.Bold,
            fontSize = 18.sp, textAlign = TextAlign.Center,
            modifier = modifier
                .wrapContentSize(Alignment.Center)
                .clickable { showDialog.value = true }
        )
        Spacer(modifier = modifier.width(32.dp))
        IconButton(
            enabled = selectedYear.value != (thisYear + maxRange),
            onClick = { selectedYear.value = selectedYear.value + 1 }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowCircleRight,
                contentDescription = stringResource(R.string.add_year),
                tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.tertiary,
                modifier = modifier.size(32.dp)
            )
        }
    }
}