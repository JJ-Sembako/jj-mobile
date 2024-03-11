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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.presentation.theme.StatusDeliveredText
import com.dr.jjsembako.core.presentation.theme.dialogMaxWidth
import com.dr.jjsembako.core.presentation.theme.dialogMinWidth

@Composable
fun SuccessDialog(
    message: String = "",
    showDialog: MutableState<Boolean>,
    modifier: Modifier
) {
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
            Icon(
                imageVector = Icons.Default.CheckCircleOutline,
                contentDescription = stringResource(R.string.success),
                tint = Color.Green,
                modifier = modifier.size(80.dp)
            )
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = stringResource(R.string.success),
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp, color = StatusDeliveredText,
                textAlign = TextAlign.Center,
                modifier = modifier.wrapContentSize(Alignment.Center)
            )
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = message,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = modifier.wrapContentSize(Alignment.Center)
            )
            Spacer(modifier = modifier.height(24.dp))
            Button(
                onClick = {
                    showDialog.value = false
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Red,
                    containerColor = Color.Red
                ),
                shape = RoundedCornerShape(50.dp),
                modifier = modifier.padding(horizontal = 5.dp, vertical = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.ok),
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SuccessDialogPreview() {
    JJSembakoTheme {
        SuccessDialog(
            message = stringResource(R.string.success_delete_product),
            showDialog = remember { mutableStateOf(true) },
            modifier = Modifier
        )
    }
}