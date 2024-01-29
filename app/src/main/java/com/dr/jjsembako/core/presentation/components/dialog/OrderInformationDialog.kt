package com.dr.jjsembako.core.presentation.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.dr.jjsembako.core.presentation.components.utils.OrderStatus
import com.dr.jjsembako.core.presentation.components.utils.PaymentStatus
import com.dr.jjsembako.core.presentation.components.utils.PotongNotaStatus
import com.dr.jjsembako.core.presentation.components.utils.ReturStatus
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun OrderInformationDialog(
    showDialog: MutableState<Boolean>,
    modifier: Modifier
) {
    val dialogMinWidth = 280.dp
    val dialogMaxWidth = 560.dp
    val index = rememberSaveable { mutableIntStateOf(0) }
    val title = arrayOf(
        stringResource(R.string.payment),
        stringResource(R.string.retur_potong_nota_schema),
        stringResource(R.string.order_status),
        stringResource(R.string.retur_status),
        stringResource(R.string.potong_nota_status),
        stringResource(R.string.payment_status)
    )

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
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(R.string.info),
                tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.tertiary,
                modifier = modifier.size(56.dp)
            )
            Spacer(modifier = modifier.height(16.dp))
            TitleInformationSection(title = title, index = index, modifier = modifier)
            Spacer(modifier = modifier.height(16.dp))
            BodyInformationSection(index = index, modifier = modifier)
        }
    }
}

@Composable
private fun TitleInformationSection(
    title: Array<String>,
    index: MutableState<Int>,
    modifier: Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Default.ArrowCircleLeft,
            contentDescription = stringResource(R.string.back),
            tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.tertiary,
            modifier = modifier
                .size(24.dp)
                .clickable {
                    if (index.value == 0) {
                        index.value = title.lastIndex
                    } else {
                        index.value = index.value.toString().toInt() - 1
                    }
                }
        )
        Spacer(modifier = modifier.height(4.dp))
        Text(
            text = title[index.value],
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = modifier.wrapContentSize(Alignment.Center)
        )
        Spacer(modifier = modifier.height(4.dp))
        Icon(
            imageVector = Icons.Default.ArrowCircleRight,
            contentDescription = stringResource(R.string.next),
            tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.tertiary,
            modifier = modifier
                .size(24.dp)
                .clickable {
                    if (index.value == title.lastIndex) {
                        index.value = 0
                    } else {
                        index.value = index.value.toString().toInt() + 1
                    }
                }
        )
    }
}

@Composable
private fun BodyInformationSection(
    index: MutableState<Int>,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (index.value) {
            0 -> {
                Text(
                    text = stringResource(R.string.info_payment),
                    fontWeight = FontWeight.Normal, fontSize = 14.sp,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }

            1 -> {
                Text(
                    text = stringResource(R.string.info_schema),
                    fontWeight = FontWeight.Normal, fontSize = 14.sp,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }

            2 -> {
                OrderStatus(status = 0, modifier = modifier)
                Spacer(modifier = modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = stringResource(R.string.become),
                    tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.tertiary,
                    modifier = modifier.size(24.dp)
                )
                Spacer(modifier = modifier.height(4.dp))
                OrderStatus(status = 1, modifier = modifier)
                Spacer(modifier = modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = stringResource(R.string.become),
                    tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.tertiary,
                    modifier = modifier.size(24.dp)
                )
                Spacer(modifier = modifier.height(4.dp))
                OrderStatus(status = 2, modifier = modifier)
                Spacer(modifier = modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = stringResource(R.string.become),
                    tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.tertiary,
                    modifier = modifier.size(24.dp)
                )
                Spacer(modifier = modifier.height(4.dp))
                OrderStatus(status = 3, modifier = modifier)
            }

            3 -> {
                ReturStatus(status = 0, modifier = modifier)
                Spacer(modifier = modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = stringResource(R.string.become),
                    tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.tertiary,
                    modifier = modifier.size(24.dp)
                )
                Spacer(modifier = modifier.height(4.dp))
                ReturStatus(status = 1, modifier = modifier)
                Spacer(modifier = modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = stringResource(R.string.become),
                    tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.tertiary,
                    modifier = modifier.size(24.dp)
                )
                Spacer(modifier = modifier.height(4.dp))
                ReturStatus(status = 2, modifier = modifier)
            }

            4 -> {
                PotongNotaStatus(status = 0, modifier = modifier)
                Spacer(modifier = modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = stringResource(R.string.become),
                    tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.tertiary,
                    modifier = modifier.size(24.dp)
                )
                Spacer(modifier = modifier.height(4.dp))
                PotongNotaStatus(status = 1, modifier = modifier)
            }

            5 -> {
                PaymentStatus(status = 0, modifier = modifier)
                Spacer(modifier = modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = stringResource(R.string.become),
                    tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.tertiary,
                    modifier = modifier.size(24.dp)
                )
                Spacer(modifier = modifier.height(4.dp))
                PaymentStatus(status = 1, modifier = modifier)
            }

            else -> {}
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun OrderInformationDialogPreview() {
    JJSembakoTheme {
        OrderInformationDialog(
            showDialog = remember { mutableStateOf(true) },
            modifier = Modifier
        )
    }
}