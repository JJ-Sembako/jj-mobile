package com.dr.jjsembako.feature_home.presentation.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.remote.response.order.OrderDataItem
import com.dr.jjsembako.core.presentation.components.card.OrderHistoryCard
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.DataMapper.mapOrderDataItemToDataOrderHistoryCard

@Composable
fun HistorySection(
    isErrorInit: Boolean = false,
    dataOrders: List<OrderDataItem?>? = null,
    context: Context,
    clipboardManager: ClipboardManager,
    onNavigateToDetail: (String) -> Unit,
    modifier: Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_empty))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )
    val composition2 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_warning))
    val progress2 by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.latest_sale), fontWeight = FontWeight.Bold
        )
        Spacer(modifier = modifier.height(16.dp))
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isErrorInit) {
                LottieAnimation(
                    enableMergePaths = true,
                    composition = composition2,
                    progress = { progress2 },
                    modifier = modifier.size(150.dp)
                )
                Spacer(modifier = modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.error),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier
                        .padding(bottom = 16.dp)
                )
            } else if (dataOrders.isNullOrEmpty()) {
                LottieAnimation(
                    enableMergePaths = true,
                    composition = composition,
                    progress = { progress },
                    modifier = modifier.size(150.dp)
                )
            } else if (dataOrders.isNotEmpty()) {
                dataOrders.forEach { data ->
                    if (data != null) {
                        OrderHistoryCard(
                            data = mapOrderDataItemToDataOrderHistoryCard(data),
                            context = context,
                            onNavigateToDetail = { id -> onNavigateToDetail(id) },
                            clipboardManager = clipboardManager,
                            modifier = modifier
                        )
                        Spacer(modifier = modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = modifier.height(48.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun HistorySectionPreview() {
    JJSembakoTheme {
        HistorySection(
            context = LocalContext.current,
            clipboardManager = LocalClipboardManager.current,
            onNavigateToDetail = {},
            modifier = Modifier
        )
    }
}