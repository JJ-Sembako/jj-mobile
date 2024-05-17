package com.dr.jjsembako.pesanan.presentation.components.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.dummy.dataCanceled
import com.dr.jjsembako.core.data.dummy.dataRetur
import com.dr.jjsembako.pesanan.domain.model.Canceled
import com.dr.jjsembako.pesanan.domain.model.Retur
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.formatRupiah
import com.dr.jjsembako.pesanan.presentation.components.fragment.PotongNotaInformationContent
import com.dr.jjsembako.pesanan.presentation.components.fragment.ReturInformationContent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReturPotongNotaInformation(
    dataCanceled: List<Canceled?>?,
    dataRetur: List<Retur?>?,
    actualTotalPrice: Long,
    showDialogCanceled: MutableState<Boolean>,
    showDialogRetur: MutableState<Boolean>,
    showDialogPreview: MutableState<Boolean>,
    previewProductName: MutableState<String>,
    previewProductImage: MutableState<String>,
    idDeleteCanceled: MutableState<String>,
    idDeleteRetur: MutableState<String>,
    statusCanceled: MutableState<Int>,
    statusRetur: MutableState<Int>,
    modifier: Modifier
) {
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf(stringResource(R.string.retur), stringResource(R.string.potong_nota))
    val pagerState = rememberPagerState { tabs.size }

    LaunchedEffect(tabIndex) {
        pagerState.animateScrollToPage(tabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            tabIndex = pagerState.currentPage
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(
            modifier = modifier
                .fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = stringResource(R.string.retur_potong_nota_detail),
            fontWeight = FontWeight.Bold, fontSize = 16.sp,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
        Divider(
            modifier = modifier
                .fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary
        )

        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = modifier.fillMaxWidth()
        ) { index ->
            when (index) {
                0 -> ReturInformationContent(
                    dataRetur, showDialogRetur, showDialogPreview, previewProductName,
                    previewProductImage, idDeleteRetur, statusRetur, modifier
                )

                1 -> PotongNotaInformationContent(
                    dataCanceled, showDialogCanceled, showDialogPreview, previewProductName,
                    previewProductImage, idDeleteCanceled, statusCanceled, modifier
                )

                else -> {}
            }
        }

        Divider(
            modifier = modifier
                .fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(R.string.final_total_price),
                fontSize = 12.sp, fontWeight = FontWeight.Normal, textAlign = TextAlign.Center,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = formatRupiah(actualTotalPrice),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }
        Divider(
            modifier = modifier
                .fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ReturPotongNotaInformationPreview() {
    JJSembakoTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReturPotongNotaInformation(
                dataCanceled = dataCanceled,
                dataRetur = dataRetur,
                actualTotalPrice = 1_500_000L,
                showDialogCanceled = remember { mutableStateOf(true) },
                showDialogRetur = remember { mutableStateOf(true) },
                showDialogPreview = remember { mutableStateOf(true) },
                previewProductName = remember { mutableStateOf("") },
                previewProductImage = remember { mutableStateOf("") },
                idDeleteCanceled = remember { mutableStateOf("") },
                idDeleteRetur = remember { mutableStateOf("") },
                statusCanceled = remember { mutableIntStateOf(0) },
                statusRetur = remember { mutableIntStateOf(0) },
                modifier = Modifier
            )
        }
    }
}