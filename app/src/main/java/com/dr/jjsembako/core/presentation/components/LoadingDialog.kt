package com.dr.jjsembako.core.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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

    Dialog(onDismissRequest = { showDialog.value = false }) {
        Card(
            modifier = modifier
                .sizeIn(
                    minWidth = dialogMinWidth, maxWidth = dialogMaxWidth
                )
                .height(300.dp)
                .padding(24.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = modifier.size(100.dp)
            )
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = stringResource(R.string.on_progress),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = stringResource(R.string.please_wait),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
            )
            Spacer(modifier = modifier.height(24.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoadingDialogPreview() {
    JJSembakoTheme {
        LoadingDialog(
            showDialog = remember { mutableStateOf(true) },
            modifier = Modifier
        )
    }
}