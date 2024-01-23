package com.dr.jjsembako.feature_auth.presentation.password_recovery

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dr.jjsembako.R
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.presentation.components.dialog.AlertErrorDialog
import com.dr.jjsembako.core.presentation.components.dialog.LoadingDialog
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.isValidNewPassword
import com.dr.jjsembako.core.utils.isValidPassword
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PemulihanKataSandiScreen(
    username: String,
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pemulihanKataSandiViewModel: PemulihanKataSandiViewModel = hiltViewModel()
    val state = pemulihanKataSandiViewModel.state.observeAsState().value
    val statusCode = pemulihanKataSandiViewModel.statusCode.observeAsState().value
    val message = pemulihanKataSandiViewModel.message.observeAsState().value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    val showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    val showErrorDialog = rememberSaveable { mutableStateOf(false) }

    var newPassword by rememberSaveable { mutableStateOf("") }
    var confNewPassword by rememberSaveable { mutableStateOf("") }

    var newPasswordVisibility by remember { mutableStateOf(false) }
    var confNewPasswordVisibility by remember { mutableStateOf(false) }

    val isValidNewPassword = rememberSaveable { mutableStateOf(false) }
    val isValidConfNewPassword = rememberSaveable { mutableStateOf(false) }

    val errMsgNewPassword = rememberSaveable { mutableStateOf("") }
    val errMsgConfNewPassword = rememberSaveable { mutableStateOf("") }

    val errPassMin8Char = stringResource(R.string.err_pass_min)
    val errPassContainWhiteSpace = stringResource(R.string.err_pass_not_whitespace)
    val errConfPassDifferent = stringResource(R.string.err_conf_pass)

    val iconNewPassword =
        if (newPasswordVisibility) painterResource(id = R.drawable.ic_visibility_on) else painterResource(
            id = R.drawable.ic_visibility_off
        )
    val iconConfNewPassword =
        if (confNewPasswordVisibility) painterResource(id = R.drawable.ic_visibility_on) else painterResource(
            id = R.drawable.ic_visibility_off
        )

    LaunchedEffect(key1 = keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }

    when (state) {
        StateResponse.LOADING -> {
            showLoadingDialog.value = true
        }

        StateResponse.ERROR -> {
            Log.e("PemulihanKataSandi", "Error")
            Log.e("PemulihanKataSandi", "state: $state")
            Log.e("PemulihanKataSandi", "Error: $message")
            Log.e("PemulihanKataSandi", "statusCode: $statusCode")
            showLoadingDialog.value = false
            showErrorDialog.value = true
            pemulihanKataSandiViewModel.setState(null)
        }

        StateResponse.SUCCESS -> {
            pemulihanKataSandiViewModel.setState(null)
            showLoadingDialog.value = false
            showErrorDialog.value = false
            onNavigateToLogin()
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
                title = { Text(stringResource(R.string.change_password)) },
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
                label = { Text(stringResource(R.string.new_password)) },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = if (newPasswordVisibility) VisualTransformation.None
                else PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { newPasswordVisibility = !newPasswordVisibility }) {
                        Icon(
                            painter = iconNewPassword,
                            contentDescription = stringResource(R.string.visible_pass)
                        )
                    }
                },
                isError = !isValidNewPassword.value,
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    if (newPassword.contains(" ")) {
                        isValidNewPassword.value = false
                        errMsgNewPassword.value = errPassContainWhiteSpace
                    } else if (!isValidPassword(newPassword)) {
                        isValidNewPassword.value = false
                        errMsgNewPassword.value = errPassMin8Char
                    } else {
                        isValidNewPassword.value = true
                        errMsgNewPassword.value = ""
                    }
                    if (!isValidNewPassword(newPassword, confNewPassword)) {
                        isValidConfNewPassword.value = false
                        errMsgConfNewPassword.value = errConfPassDifferent
                    } else {
                        isValidConfNewPassword.value = true
                        errMsgConfNewPassword.value = ""
                    }
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            )
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                text = errMsgNewPassword.value,
                fontSize = 14.sp,
                color = Color.Red
            )


            OutlinedTextField(
                label = { Text(stringResource(R.string.conf_new_password)) },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = if (confNewPasswordVisibility) VisualTransformation.None
                else PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = {
                        confNewPasswordVisibility = !confNewPasswordVisibility
                    }) {
                        Icon(
                            painter = iconConfNewPassword,
                            contentDescription = stringResource(R.string.visible_pass)
                        )
                    }
                },
                isError = !isValidConfNewPassword.value,
                value = confNewPassword,
                onValueChange = {
                    confNewPassword = it
                    if (confNewPassword.contains(" ")) {
                        isValidConfNewPassword.value = false
                        errMsgConfNewPassword.value = errPassContainWhiteSpace
                    } else if (!isValidPassword(confNewPassword)) {
                        isValidConfNewPassword.value = false
                        errMsgConfNewPassword.value = errPassMin8Char
                    } else if (!isValidNewPassword(newPassword, confNewPassword)) {
                        isValidConfNewPassword.value = false
                        errMsgConfNewPassword.value = errConfPassDifferent
                    } else {
                        isValidConfNewPassword.value = true
                        errMsgConfNewPassword.value = ""
                    }
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            )
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                text = errMsgConfNewPassword.value,
                fontSize = 14.sp,
                color = Color.Red
            )

            Spacer(modifier = modifier.height(16.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    pemulihanKataSandiViewModel.handleUpdatePasswordFromRecovery(
                        username,
                        newPassword,
                        confNewPassword
                    )
                },
                enabled = isValidNewPassword.value && isValidConfNewPassword.value,
                modifier = modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(stringResource(R.string.update))
            }
            Spacer(modifier = modifier.height(128.dp))

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

@Preview(showBackground = true)
@Composable
private fun PemulihanKataSandiScreenPreview() {
    JJSembakoTheme {
        PemulihanKataSandiScreen(
            username = "",
            onNavigateBack = {},
            onNavigateToLogin = {}
        )
    }
}