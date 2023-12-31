package com.dr.jjsembako.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun AlertDeleteDialog(
    onDelete: () -> Unit,
    showDialog: MutableState<Boolean>,
    modifier: Modifier
) {
    AlertDialog(
        shape = RoundedCornerShape(20.dp),
        onDismissRequest = { showDialog.value = false },
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = stringResource(R.string.alert),
                tint = Color.Red,
                modifier = modifier.size(56.dp)
            )
        },
        title = { Text(text = stringResource(R.string.are_you_sure)) },
        text = { Text(text = stringResource(R.string.content_del)) },
        dismissButton = {
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
        },
        confirmButton = {
            Button(
                onClick = {
                    showDialog.value = false
                    onDelete()
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Red,
                    containerColor = Color.Red
                ),
                shape = RoundedCornerShape(50.dp),
                modifier = modifier.padding(horizontal = 5.dp, vertical = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.yes_delete),
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                )
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
private fun AlertDeleteDialogPreview() {
    JJSembakoTheme {
        AlertDeleteDialog(
            onDelete = {},
            showDialog = remember { mutableStateOf(true) },
            modifier = Modifier
        )
    }
}
