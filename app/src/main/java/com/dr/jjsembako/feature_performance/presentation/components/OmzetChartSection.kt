package com.dr.jjsembako.feature_performance.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dr.jjsembako.core.utils.chart.rememberMarker
import com.dr.jjsembako.core.utils.formatShortRupiah
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Composable
fun OmzetChartSection(
    modifier: Modifier
) {
    val chartEntryModel = entryModelOf(
        450_000L, 1_250_000L, 875_000L, 1_625_000L,
        250_000L, 2_250_000L, 815_000L, 1_223_000L,
        404_000L, 7_955_000L, 815_000L, 1_015_000L
    )
    val time = listOf(
        "DES 2023", "JAN 2024", "FEB 2024", "MAR 2024",
        "APR 2024", "MEI 2024", "JUN 2024", "JUL 2024",
        "AGU 2024", "SEP 2024", "OKT 2024", "NOV 2024"
    )
    val marker = rememberMarker()
    Chart(
        chart = lineChart(),
        model = chartEntryModel,
        startAxis = rememberStartAxis(
            title = "Waktu",
            valueFormatter = { value, _ -> formatShortRupiah(value.toLong()) }
        ),
        bottomAxis = rememberBottomAxis(
            title = "Omzet",
            valueFormatter = { value, _ ->  time[value.toInt()] }
        ),
        marker = marker,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(240.dp)
    )
}