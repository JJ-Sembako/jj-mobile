package com.dr.jjsembako.feature_performance.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dr.jjsembako.core.data.remote.response.performance.Omzet
import com.dr.jjsembako.core.utils.chart.rememberMarker
import com.dr.jjsembako.core.utils.formatShortRupiah
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf

@Composable
fun OmzetChartSection(
    omzet: List<Omzet?>?,
    time: List<String>,
    modifier: Modifier
) {
    val marker = rememberMarker(time)
    val modelProducer = remember { ChartEntryModelProducer() }

    LaunchedEffect(Unit, omzet) {
        if (!omzet.isNullOrEmpty()) {
            modelProducer.setEntries(omzet.mapIndexedNotNull { index, omzetData ->
                omzetData?.let { omzet ->
                    entryOf(index, omzet.omzet)
                }
            })
        }
    }

    Chart(
        chart = lineChart(),
        chartModelProducer = modelProducer,
        startAxis = rememberStartAxis(
            title = "Waktu",
            valueFormatter = { value, _ -> formatShortRupiah(value.toLong()) }
        ),
        bottomAxis = rememberBottomAxis(
            title = "Omzet",
            valueFormatter = { value, _ -> time[value.toInt()] }
        ),
        marker = marker,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(240.dp)
    )
}