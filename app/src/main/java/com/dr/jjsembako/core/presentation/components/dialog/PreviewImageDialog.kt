package com.dr.jjsembako.core.presentation.components.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun PreviewImageDialog(
    name: String,
    image: String,
    showDialog: MutableState<Boolean>,
    modifier: Modifier
) {
    Dialog(
        onDismissRequest = { showDialog.value = false }) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxWidth()
                .heightIn(max = 560.dp)
                .background(Color.Black),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showDialog.value = false }) {
                    Icon(
                        imageVector = Icons.Default.Close, modifier = modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = stringResource(R.string.close)
                    )
                }
                Spacer(modifier = modifier.width(8.dp))
                Text(
                    text = name, fontWeight = FontWeight.Bold,
                    fontSize = 14.sp, textAlign = TextAlign.Center, color = Color.White,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }
            Spacer(modifier = modifier.height(16.dp))
            if (image.isEmpty() || image.contains("default")) {
                Image(
                    painter = painterResource(id = R.drawable.ic_default),
                    contentDescription = stringResource(R.string.product_description, name),
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .padding(8.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                )
            } else {
                AsyncImage(
                    model = image,
                    contentDescription = stringResource(R.string.product_description, name),
                    contentScale = ContentScale.FillBounds,
                    error = painterResource(id = R.drawable.ic_error),
                    modifier = modifier
                        .padding(8.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                )
            }
            Spacer(modifier = modifier.height(16.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewImageDialogPreview() {
    JJSembakoTheme {
        PreviewImageDialog(
            name = "Cake",
            image = "",
            showDialog = remember { mutableStateOf(true) },
            modifier = Modifier
        )
    }
}