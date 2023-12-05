package com.dr.jjsembako.ui.feature_customer.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.domain.model.Customer
import com.dr.jjsembako.core.model.FilterOption
import com.dr.jjsembako.core.components.BottomSheetCustomer
import com.dr.jjsembako.core.components.CustomerInfo
import com.dr.jjsembako.core.components.SearchFilter
import com.dr.jjsembako.core.theme.JJSembakoTheme

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PelangganScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetailCust: (String) -> Unit,
    onNavigateToAddCust: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showSheet = remember { mutableStateOf(false) }
    var (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    var searchQuery = remember { mutableStateOf("") }
    var activeSearch = remember { mutableStateOf(false) }

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
            if (!activeSearch.value) {
                FloatingActionButton(
                    onClick = { onNavigateToAddCust() },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = stringResource(R.string.add_new_cust)
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        keyboardController?.hide()
                        activeSearch.value = false
                        focusManager.clearFocus()
                    })
                .padding(contentPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchFilter(
                placeholder = stringResource(R.string.search_cust),
                activeSearch,
                searchQuery,
                openFilter = { showSheet.value = !showSheet.value },
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                items(items = custList, itemContent = { cust ->
                    CustomerInfo(
                        onNavigateToDetailCust = { onNavigateToDetailCust(cust.id) },
                        customer = cust,
                        modifier = modifier
                    )
                    Spacer(modifier = modifier.height(8.dp))
                })
            }
            if (showSheet.value) {
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
    Customer(
        "bcde-123",
        "Yulianti",
        "Warung Sejahter",
        "Jl. Nusa Indah 3, Belimbing, Jambu, Sayuran, Tumbuhan",
        "https://gmaps.com/123123",
        "081234567890",
        500_000L
    ),
    Customer(
        "klmn-456",
        "Susi",
        "Toko Susi",
        "Jl. Kalibata Raya No. 5 Gang Cenderawasih, Sapi, Gajah, Harimau, Mamalia, Hewan",
        "https://gmaps.com/123123",
        "081234567890",
        0L
    ),
    Customer(
        "rasa-123",
        "Dewi",
        "Toko Dewi Pojok",
        "Jl. Nusa Indah 3, Belimbing, Jambu, Sayuran, Tumbuhan",
        "https://gmaps.com/123123",
        "081234567890",
        300_000L
    ),
    Customer(
        "kjas-456",
        "Tukimin",
        "Toko Min Jo",
        "Jl. Kalibata Raya No. 5 Gang Cenderawasih, Sapi, Gajah, Harimau, Mamalia, Hewan",
        "https://gmaps.com/123123",
        "081234567890",
        0L
    ),
    Customer(
        "asad-123",
        "Heru",
        "Warung Pojok",
        "Jl. Nusa Indah 3, Belimbing, Jambu, Sayuran, Tumbuhan",
        "https://gmaps.com/123123",
        "081234567890",
        500_000L
    ),
    Customer(
        "pqrs-456",
        "Sulistiati",
        "Toko Sulis",
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
        PelangganScreen(
            onNavigateBack = {},
            onNavigateToDetailCust = {},
            onNavigateToAddCust = {},
            modifier = Modifier
        )
    }
}