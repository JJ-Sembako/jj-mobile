package com.dr.jjsembako.feature_order.presentation.create_order

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.feature_order.presentation.components.SelectCustomer
import com.dr.jjsembako.feature_order.presentation.components.SelectPayment
import com.dr.jjsembako.feature_order.presentation.components.SelectProduct
import com.dr.jjsembako.feature_order.presentation.components.TotalPayment

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun BuatPesananScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSelectCustomer: () -> Unit,
    onNavigateToSelectProduct: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = { Text(stringResource(R.string.create_order)) },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateBack()
                        keyboardController?.hide()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        onNavigateBack()
                        keyboardController?.hide()
                    }) {
                        Icon(
                            Icons.Default.Check,
                            stringResource(R.string.finish),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    })
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(id = R.string.clear),
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = modifier.clickable {}
                )
                Spacer(modifier = modifier.width(16.dp))
                Icon(
                    Icons.Default.DeleteSweep,
                    contentDescription = stringResource(R.string.clear_data),
                    tint = Color.Red,
                    modifier = modifier.clickable {}
                )
            }
            SelectCustomer(
                customer = null,
                onSelectCustomer = { onNavigateToSelectCustomer() },
                modifier = modifier
            )
            SelectPayment(
                selectedOption = FilterOption("Tempo", "tempo"),
                onOptionSelected = {},
                modifier = modifier
            )
            SelectProduct(onSelectProduct = { onNavigateToSelectProduct() }, modifier = modifier)
            TotalPayment(totalPrice = 1525750, modifier = modifier)
            Spacer(modifier = modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BuatPesananScreenPreview() {
    JJSembakoTheme {
        BuatPesananScreen(
            onNavigateBack = {},
            onNavigateToSelectCustomer = {},
            onNavigateToSelectProduct = {}
        )
    }
}