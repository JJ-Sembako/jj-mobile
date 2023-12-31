package com.dr.jjsembako.feature_customer.presentation.edit

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dr.jjsembako.R
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.data.remote.response.customer.DataCustomer
import com.dr.jjsembako.core.presentation.components.AlertErrorDialog
import com.dr.jjsembako.core.presentation.components.ErrorScreen
import com.dr.jjsembako.core.presentation.components.LoadingDialog
import com.dr.jjsembako.core.presentation.components.LoadingScreen
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.isValidLinkMaps
import com.dr.jjsembako.core.utils.isValidPhoneNumber
import kotlinx.coroutines.launch

@Composable
fun EditPelangganScreen(
    idCust: String,
    onNavigateToDetailCust: () -> Unit,
    openMaps: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val editPelangganViewModel: EditPelangganViewModel = hiltViewModel()
    val stateFirst = editPelangganViewModel.stateFirst.observeAsState().value
    val statusCode = editPelangganViewModel.statusCode.observeAsState().value
    val message = editPelangganViewModel.message.observeAsState().value
    val customerData = editPelangganViewModel.customerData

    // Set id for the first time Composable is rendered
    LaunchedEffect(idCust) {
        editPelangganViewModel.setId(idCust)
    }

    when (stateFirst) {
        StateResponse.LOADING -> {
            LoadingScreen(modifier = modifier)
        }

        StateResponse.ERROR -> {
            Log.e("EditPelanggan", "Error")
            Log.e("EditPelanggan", "state: $stateFirst")
            Log.e("EditPelanggan", "Error: $message")
            Log.e("EditPelanggan", "statusCode: $statusCode")
            ErrorScreen(
                onNavigateBack = { onNavigateToDetailCust() },
                onReload = {
                    editPelangganViewModel.fetchDetailCustomer(idCust)
                },
                message = message ?: "Unknown error",
                modifier = modifier
            )
        }

        StateResponse.SUCCESS -> {
            if (customerData == null) {
                ErrorScreen(
                    onNavigateBack = { onNavigateToDetailCust() },
                    onReload = { editPelangganViewModel.fetchDetailCustomer(idCust) },
                    message = "Server Error",
                    modifier = modifier
                )
            } else {
                EditPelangganContent(
                    cust = customerData,
                    editPelangganViewModel = editPelangganViewModel,
                    onNavigateToDetailCust = { onNavigateToDetailCust() },
                    openMaps = { url -> openMaps(url) },
                    modifier = modifier
                )
            }
        }

        else -> {}
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun EditPelangganContent(
    cust: DataCustomer,
    editPelangganViewModel: EditPelangganViewModel,
    onNavigateToDetailCust: () -> Unit,
    openMaps: (String) -> Unit,
    modifier: Modifier
) {
    val stateSecond = editPelangganViewModel.stateSecond.observeAsState().value
    val statusCode = editPelangganViewModel.statusCode.observeAsState().value
    val message = editPelangganViewModel.message.observeAsState().value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    var showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    var showErrorDialog = rememberSaveable { mutableStateOf(false) }

    var name by rememberSaveable { mutableStateOf(cust.name) }
    var shopName by rememberSaveable { mutableStateOf(cust.shopName) }
    var phoneNumber by rememberSaveable { mutableStateOf(cust.phoneNumber) }
    var address by rememberSaveable { mutableStateOf(cust.address) }
    var mapsLink by rememberSaveable { mutableStateOf(cust.gmapsLink) }

    var isValidName = rememberSaveable { mutableStateOf(true) }
    var isValidShopName = rememberSaveable { mutableStateOf(true) }
    var isValidPhoneNumber = rememberSaveable { mutableStateOf(true) }
    var isValidAddress = rememberSaveable { mutableStateOf(true) }
    var isValidMapsLink = rememberSaveable { mutableStateOf(true) }

    var errMsgName = rememberSaveable { mutableStateOf("") }
    var errMsgShopName = rememberSaveable { mutableStateOf("") }
    var errMsgPhoneNumber = rememberSaveable { mutableStateOf("") }
    var errMsgAddress = rememberSaveable { mutableStateOf("") }
    var errMsgMapsLink = rememberSaveable { mutableStateOf("") }

    val errInputEmpty = stringResource(R.string.err_input_empty)
    val errInvalidLink = stringResource(R.string.err_invalid_link)
    val errInvalidPhoneNumber = stringResource(R.string.err_invalid_phone_number)

    LaunchedEffect(key1 = keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }

    when (stateSecond) {
        StateResponse.LOADING -> {
            LoadingScreen(modifier = modifier)
        }

        StateResponse.ERROR -> {
            Log.e("EditPelanggan", "Error")
            Log.e("EditPelanggan", "state: $stateSecond")
            Log.e("EditPelanggan", "Error: $message")
            Log.e("EditPelanggan", "statusCode: $statusCode")
            showLoadingDialog.value = false
            showErrorDialog.value = true
            editPelangganViewModel.setStateSecond(null)
        }

        StateResponse.SUCCESS -> {
            showLoadingDialog.value = false
            showErrorDialog.value = false
            editPelangganViewModel.setStateSecond(null)
            onNavigateToDetailCust()
        }

        else -> {}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = { Text(stringResource(R.string.add_new_cust)) },
                navigationIcon = {
                    IconButton(onClick = {
                        keyboardController?.hide()
                        onNavigateToDetailCust()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
            )
        }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { keyboardController?.hide() })
                .padding(contentPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                label = { Text(stringResource(R.string.name)) },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                isError = !isValidName.value,
                value = name,
                onValueChange = {
                    name = it.trimStart()
                    if (name.isEmpty()) {
                        isValidName.value = false
                        errMsgName.value = errInputEmpty
                    } else {
                        isValidName.value = true
                        errMsgName.value = ""
                    }
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                text = errMsgName.value,
                fontSize = 14.sp,
                color = Color.Red
            )

            OutlinedTextField(
                label = { Text(stringResource(R.string.shop_name)) },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                isError = !isValidShopName.value,
                value = shopName,
                onValueChange = {
                    shopName = it.trimStart()
                    if (shopName.isEmpty()) {
                        isValidShopName.value = false
                        errMsgShopName.value = errInputEmpty
                    } else {
                        isValidShopName.value = true
                        errMsgShopName.value = ""
                    }
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                text = errMsgShopName.value,
                fontSize = 14.sp,
                color = Color.Red
            )

            OutlinedTextField(
                label = { Text(stringResource(R.string.phone_number)) },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                isError = !isValidPhoneNumber.value,
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                    if (phoneNumber.isEmpty()) {
                        isValidPhoneNumber.value = false
                        errMsgPhoneNumber.value = errInputEmpty
                    } else if (!isValidPhoneNumber(phoneNumber)) {
                        isValidPhoneNumber.value = false
                        errMsgPhoneNumber.value = errInvalidPhoneNumber
                    } else {
                        isValidPhoneNumber.value = true
                        errMsgPhoneNumber.value = ""
                    }
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                text = errMsgPhoneNumber.value,
                fontSize = 14.sp,
                color = Color.Red
            )

            OutlinedTextField(
                label = { Text(stringResource(R.string.address)) },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                isError = !isValidAddress.value,
                value = address,
                onValueChange = {
                    address = it.trimStart()
                    if (address.isEmpty()) {
                        isValidAddress.value = false
                        errMsgAddress.value = errInputEmpty
                    } else {
                        isValidAddress.value = true
                        errMsgAddress.value = ""
                    }
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                text = errMsgAddress.value,
                fontSize = 14.sp,
                color = Color.Red
            )

            OutlinedTextField(
                label = { Text(stringResource(R.string.maps_link)) },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                isError = !isValidMapsLink.value,
                value = mapsLink,
                onValueChange = {
                    mapsLink = it.trimStart()
                    if (mapsLink.isEmpty()) {
                        isValidMapsLink.value = false
                        errMsgMapsLink.value = errInputEmpty
                    } else if (!isValidLinkMaps(mapsLink)) {
                        isValidMapsLink.value = false
                        errMsgMapsLink.value = errInvalidLink
                    } else {
                        isValidMapsLink.value = true
                        errMsgMapsLink.value = ""
                    }
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                text = errMsgMapsLink.value,
                fontSize = 14.sp,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    openMaps(mapsLink)
                },
                enabled = isValidMapsLink.value && stateSecond != StateResponse.LOADING,
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

            Spacer(modifier = Modifier.height(24.dp))
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
                        onNavigateToDetailCust()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    modifier = modifier
                        .width(120.dp)
                        .height(56.dp)
                ) {
                    Text(stringResource(R.string.cancel))
                }
                Spacer(modifier = modifier.width(48.dp))
                Button(
                    onClick = {
                        keyboardController?.hide()
                        editPelangganViewModel.handleUpdateCustomer(
                            cust.id,
                            name,
                            shopName,
                            address,
                            mapsLink,
                            phoneNumber
                        )
                    },
                    enabled = isValidName.value && isValidShopName.value && isValidPhoneNumber.value && isValidAddress.value && isValidMapsLink.value && stateSecond != StateResponse.LOADING,
                    modifier = modifier
                        .width(120.dp)
                        .height(56.dp)
                ) {
                    Text(stringResource(R.string.save))
                }
            }

            if (showLoadingDialog.value) {
                LoadingDialog(showLoadingDialog, modifier)
            }

            if (showErrorDialog.value) {
                AlertErrorDialog(
                    message = message ?: stringResource(id = R.string.error_msg_default),
                    showDialog = showErrorDialog,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun EditPelangganScreenPreview() {
    JJSembakoTheme {
        EditPelangganScreen(idCust = "", onNavigateToDetailCust = {}, openMaps = {})
    }
}