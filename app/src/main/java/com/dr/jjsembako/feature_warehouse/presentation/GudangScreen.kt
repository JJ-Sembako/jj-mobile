package com.dr.jjsembako.feature_warehouse.presentation

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.model.FilterOption
import com.dr.jjsembako.core.data.model.Product
import com.dr.jjsembako.core.presentation.components.BottomSheetProduct
import com.dr.jjsembako.core.presentation.components.SearchFilter
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GudangScreen(onNavigateBack: () -> Unit, modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var showSheet = remember { mutableStateOf(false) }
    val checkBoxResult = rememberSaveable { mutableStateListOf<String>() }
    val checkBoxStates = rememberSaveable { mutableStateMapOf<String, Boolean>() }
    checkBoxResult.addAll(option.map { it.value })
    checkBoxStates.putAll(option.map { it.value to true })
    var searchQuery = rememberSaveable { mutableStateOf("") }
    var activeSearch = remember { mutableStateOf(false) }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_empty))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = { Text(stringResource(R.string.warehouse)) },
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
                searchFunction = { },
                openFilter = { },
                modifier = modifier
            )
            Spacer(modifier = modifier.height(16.dp))

            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
            ){
                items(items = dataDummy, itemContent = { product ->
                    Text(text = product.name)
                    Spacer(modifier = modifier.height(8.dp))
                })
            }

            if (showSheet.value) {
                BottomSheetProduct(
                    optionList = option,
                    checkBoxResult = checkBoxResult,
                    checkBoxStates = checkBoxStates,
                    showSheet = showSheet,
                    modifier = modifier
                )
            }
        }
    }
}

private val option = listOf(
    FilterOption("Beras", "beras"),
    FilterOption("Minyak", "minyak"),
    FilterOption("Gula", "gula")
)

private val dataDummy = listOf(
    Product(
        id = "1",
        name = "Beras",
        category = "beras",
        stock = 10,
        image = ""
    ),
    Product(
        id = "2",
        name = "Minyak",
        category = "minyak",
        stock = 10,
        image = ""
    ),
    Product(
        id = "3",
        name = "Gula",
        category = "gula",
        stock = 10,
        image = ""
    )
)

@Preview(showBackground = true)
@Composable
fun GudangScreenPreview() {
    JJSembakoTheme {
        GudangScreen(
            onNavigateBack = {}
        )
    }
}