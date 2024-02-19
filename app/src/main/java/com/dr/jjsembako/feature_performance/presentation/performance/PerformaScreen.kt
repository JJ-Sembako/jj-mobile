package com.dr.jjsembako.feature_performance.presentation.performance

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.getCurrentYearMonthInGmt7
import com.dr.jjsembako.feature_performance.presentation.components.HeaderMonthYearSection
import com.dr.jjsembako.feature_performance.presentation.components.MonthYearPickerDialog
import com.dr.jjsembako.feature_performance.presentation.components.OmzetChartSection

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PerformaScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tag = "PerformaScreen"
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val selectedYear = rememberSaveable { mutableIntStateOf(0) }
    val selectedMonth = rememberSaveable { mutableIntStateOf(0) }
    val thisYear = rememberSaveable { mutableIntStateOf(0) }
    val maxRange = 10
    val showYearPickerDialog = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (thisYear.intValue == 0) {
            val time = getCurrentYearMonthInGmt7()
            selectedYear.intValue = time[0]
            thisYear.intValue = time[0]
            selectedMonth.intValue = time[1]
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = { Text(stringResource(R.string.performance)) },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateBack()
                        keyboardController?.hide()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    })
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.omzet_last_year), fontWeight = FontWeight.Bold,
                fontSize = 18.sp, textAlign = TextAlign.Center,
                modifier = modifier
                    .wrapContentSize(Alignment.Center)
                    .padding(top = 16.dp)
            )
            Spacer(modifier = modifier.height(32.dp))
            OmzetChartSection(modifier)
            Spacer(modifier = modifier.height(32.dp))
            HeaderMonthYearSection(
                thisYear = thisYear.intValue,
                maxRange = maxRange,
                selectedYear = selectedYear,
                selectedMonth = selectedMonth,
                showDialog = showYearPickerDialog,
                modifier = modifier
            )
            Spacer(modifier = modifier.height(32.dp))

            if (showYearPickerDialog.value) {
                Log.d(tag, "Show Year Picker Dialog")
                MonthYearPickerDialog(
                    thisYear = thisYear.intValue,
                    maxRange = maxRange,
                    selectedYear = selectedYear,
                    selectedMonth = selectedMonth,
                    showDialog = showYearPickerDialog,
                    modifier = modifier
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PerformaScreenPreview() {
    JJSembakoTheme {
        PerformaScreen(
            onNavigateBack = {},
        )
    }
}