package com.dr.jjsembako.feature_customer.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.isValidLinkMaps
import com.dr.jjsembako.core.utils.isValidPhoneNumber

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomerButtonInfo(
    cust: DataCustomer,
    openMaps: (String) -> Unit,
    call: (String) -> Unit,
    chatWA: (String) -> Unit,
    modifier: Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = modifier.height(16.dp))
        Divider(
            modifier = modifier
                .fillMaxWidth()
                .width(1.dp), color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = modifier.height(16.dp))
        Button(
            onClick = {
                keyboardController?.hide()
                openMaps(cust.gmapsLink)
            },
            enabled = isValidLinkMaps(cust.gmapsLink),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            modifier = modifier
                .height(56.dp)
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = stringResource(R.string.view_maps),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = modifier.padding(end = 4.dp)
            )
            Text(stringResource(R.string.view_maps))
        }
        Spacer(modifier = modifier.height(16.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    keyboardController?.hide()
                    call(cust.phoneNumber)
                },
                enabled = isValidPhoneNumber(cust.phoneNumber),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = modifier
                    .height(56.dp)
                    .width(150.dp)
            ) {
                Icon(
                    Icons.Default.Call,
                    contentDescription = stringResource(R.string.call),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = modifier.padding(end = 4.dp)
                )
                Spacer(modifier = modifier.width(4.dp))
                Text(stringResource(R.string.call))
            }
            Spacer(modifier = modifier.width(8.dp))
            Button(
                onClick = {
                    keyboardController?.hide()
                    chatWA(cust.phoneNumber)
                },
                enabled = isValidPhoneNumber(cust.phoneNumber),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = modifier
                    .height(56.dp)
                    .width(150.dp)
            ) {
                Icon(
                    Icons.Default.Chat,
                    contentDescription = stringResource(R.string.chat),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = modifier.padding(end = 4.dp)
                )
                Spacer(modifier = modifier.width(4.dp))
                Text(stringResource(R.string.chat))
            }
        }
        Spacer(modifier = modifier.height(16.dp))
        Divider(
            modifier = modifier
                .fillMaxWidth()
                .width(1.dp), color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = modifier.height(16.dp))
    }
}

@Composable
@Preview(showBackground = true)
private fun CustomerButtonInfoScreenPreview() {
    JJSembakoTheme {
        CustomerButtonInfo(
            cust = DataCustomer(
                "abcd-123",
                "Bambang",
                "Toko Makmur",
                "Jl. Nusa Indah 3, Belimbing, Jambu, Sayuran, Tumbuhan",
                "https:://maps.app.goo.gl/MQBEcptYvdYfBaNc9",
                "081234567890",
                1_500_000L
            ),
            openMaps = {},
            call = {},
            chatWA = {},
            modifier = Modifier
        )
    }
}