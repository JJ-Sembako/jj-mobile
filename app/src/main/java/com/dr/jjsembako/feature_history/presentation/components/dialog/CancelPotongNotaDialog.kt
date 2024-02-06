package com.dr.jjsembako.feature_history.presentation.components.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.presentation.theme.dialogMaxWidth
import com.dr.jjsembako.core.presentation.theme.dialogMinWidth

@Composable
fun CancelPotongNotaDialog(
    status: Int,
    showDialog: MutableState<Boolean>,
    modifier: Modifier
) {
    if (status == 0) CancelPotongNotaConfirmation(showDialog, modifier)
    else CancelPotongNotaError(showDialog, modifier)
}

@Composable
private fun CancelPotongNotaError(
    showDialog: MutableState<Boolean>,
    modifier: Modifier
) {
    val message = stringResource(R.string.err_del_pn)

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
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = stringResource(R.string.action_denied),
                tint = Color.Red,
                modifier = modifier.size(80.dp)
            )
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = stringResource(R.string.action_denied),
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
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
private fun CancelPotongNotaConfirmation(
    showDialog: MutableState<Boolean>,
    modifier: Modifier
) {
    val checkBoxStates = remember { mutableStateOf(false) }

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
                imageVector = Icons.Default.Warning,
                contentDescription = stringResource(R.string.cancel_potong_nota),
                tint = Color.Red,
                modifier = modifier.size(80.dp)
            )
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = stringResource(R.string.are_you_sure),
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = modifier.wrapContentSize(Alignment.Center)
            )
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = stringResource(R.string.confirm_del_pn_text),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = modifier.wrapContentSize(Alignment.Center)
            )
            Spacer(modifier = modifier.height(16.dp))
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = checkBoxStates.value,
                        onValueChange = { checkBoxStates.value = it }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checkBoxStates.value,
                    onCheckedChange = { checkBoxStates.value = it },
                    modifier = modifier.padding(all = Dp(value = 8F))
                )
                Text(
                    text = stringResource(R.string.iam_aware), fontWeight = FontWeight.Normal,
                    fontSize = 12.sp, modifier = modifier.padding(start = 8.dp),
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }
            Spacer(modifier = modifier.height(24.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = { showDialog.value = false },
                    shape = RoundedCornerShape(50.dp),
                    border = BorderStroke(1.dp, Color.Red),
                    modifier = modifier.padding(horizontal = 5.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = modifier.width(32.dp))
                Button(
                    enabled = checkBoxStates.value,
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
                        text = stringResource(R.string.yes_sure),
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CancelPotongNotaDialogPreview() {
    JJSembakoTheme {
        CancelPotongNotaDialog(
            status = 0,
            showDialog = remember { mutableStateOf(true) },
            modifier = Modifier
        )
    }
}