package com.dr.jjsembako.core.presentation.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun LoadingDialog(
    showDialog: MutableState<Boolean>,
    modifier: Modifier
) {
    val dialogMinWidth = 280.dp
    val dialogMaxWidth = 560.dp

    Dialog(
        onDismissRequest = { showDialog.value = false }) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .sizeIn(
                    minWidth = dialogMinWidth,
                    maxWidth = dialogMaxWidth
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = modifier.height(16.dp))
            CircularProgressIndicator(
                modifier = modifier.size(100.dp)
            )
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = stringResource(R.string.on_progress),
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = modifier.wrapContentSize(Alignment.Center)
            )
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = stringResource(R.string.please_wait),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = modifier.wrapContentSize(Alignment.Center)
            )
            Spacer(modifier = modifier.height(24.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun LoadingDialogPreview() {
    JJSembakoTheme {
        LoadingDialog(
            showDialog = remember { mutableStateOf(true) },
            modifier = Modifier
        )
    }
}