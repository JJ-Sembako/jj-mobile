package com.dr.jjsembako.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun ErrorScreen(
    onNavigateBack: () -> Unit,
    message: String = "",
    showButton: Boolean = true,
    modifier: Modifier,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_warning))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                enableMergePaths = true,
                composition = composition,
                progress = { progress },
                modifier = modifier.size(150.dp)
            )

            Spacer(modifier = modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.error),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .padding(bottom = 16.dp)
            )
            Text(
                text = message,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                modifier = modifier
                    .padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if(showButton){
                Button(
                    onClick = { onNavigateBack() },
                    modifier = modifier
                        .height(56.dp)
                ) {
                    Text(stringResource(R.string.back))
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ErrorScreenPreview() {
    JJSembakoTheme {
        ErrorScreen(onNavigateBack = {}, message = "Data tidak ditemukan.", modifier = Modifier)
    }
}