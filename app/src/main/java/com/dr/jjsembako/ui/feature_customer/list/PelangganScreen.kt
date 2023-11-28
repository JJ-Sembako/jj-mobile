package com.dr.jjsembako.ui.feature_customer.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.domain.model.Customer
import com.dr.jjsembako.core.model.FilterOption
import com.dr.jjsembako.ui.components.BottomSheetCustomer
import com.dr.jjsembako.ui.components.CustomerInfo
import com.dr.jjsembako.ui.components.SearchFilter
import com.dr.jjsembako.ui.theme.JJSembakoTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PelangganScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    var showSheet = remember { mutableStateOf(false) }
    var (selectedOption, onOptionSelected) = remember { mutableStateOf<FilterOption>(radioOptions[0]) }

    LaunchedEffect(key1 = keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = { Text(stringResource(R.string.cust)) },
                navigationIcon = {
                    IconButton(onClick = {
                        keyboardController?.hide()
                        onNavigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_new_cust))
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { contentPadding ->
        Column(
            modifier = modifier
//                .verticalScroll(scrollState)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { keyboardController?.hide() })
                .padding(contentPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchFilter(openFilter = {showSheet.value = !showSheet.value}, modifier = modifier)
            Spacer(modifier = modifier.height(16.dp))
            custList.forEach { cust ->
                CustomerInfo(customer = cust, modifier = modifier)
                Spacer(modifier = modifier.height(8.dp))
            }
            if(showSheet.value){
                BottomSheetCustomer(
                    optionList = radioOptions,
                    selectedOption = selectedOption,
                    onOptionSelected = onOptionSelected,
                    showSheet = showSheet,
                    modifier = modifier
                )
            }
        }
    }
}

private val custList = listOf(
    Customer(
        "abcd-123",
        "Bambang",
        "Toko Makmur",
        "Jl. Nusa Indah 3, Belimbing, Jambu, Sayuran, Tumbuhan",
        "https://gmaps.com/123123",
        "081234567890",
        1_500_000L
    ),
    Customer(
        "wxyz-456",
        "Budiono",
        "Toko Murah",
        "Jl. Kalibata Raya No. 5 Gang Cenderawasih, Sapi, Gajah, Harimau, Mamalia, Hewan",
        "https://gmaps.com/123123",
        "081234567890",
        0L
    ),
)

private val radioOptions = listOf(
    FilterOption("Semua Pelanggan", "semua"),
    FilterOption("Pelanggan Saya", "pelangganku")
)

@Composable
@Preview(showBackground = true)
fun PelangganScreenPreview() {
    JJSembakoTheme {
        PelangganScreen(onNavigateBack = {}, modifier = Modifier)
    }
}