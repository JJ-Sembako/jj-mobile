package com.dr.jjsembako.ui.feature_auth.login

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dr.jjsembako.R
import com.dr.jjsembako.core.utils.isValidPassword
import com.dr.jjsembako.core.utils.isValidUsername
import com.dr.jjsembako.core.utils.rememberImeState
import com.dr.jjsembako.ui.theme.JJSembakoTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToCheckUsername: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    var isValidUsername = rememberSaveable { mutableStateOf(false) }
    var isValidPassword = rememberSaveable { mutableStateOf(false) }

    var errMsgUsername = rememberSaveable { mutableStateOf("") }
    var errMsgPassword = rememberSaveable { mutableStateOf("") }

    val msgError = listOf(
        stringResource(R.string.err_username),
        stringResource(R.string.err_pass_min),
        stringResource(R.string.err_pass_not_whitespace)
    )

    var icon =
        if (passwordVisibility) painterResource(id = R.drawable.ic_visibility_on) else painterResource(
            id = R.drawable.ic_visibility_off
        )
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_hi))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue, tween(300))
        }
    }

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { keyboardController?.hide() })
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            enableMergePaths = true,
            composition = composition,
            progress = { progress },
            modifier = modifier.size(150.dp)
        )

        Spacer(modifier = modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.welcome),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            label = { Text(stringResource(R.string.username)) },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            isError = !isValidUsername.value,
            value = username,
            onValueChange = {
                username = it
                if (!isValidUsername(username)) {
                    isValidUsername.value = false
                    errMsgUsername.value = msgError[0]
                } else {
                    isValidUsername.value = true
                    errMsgUsername.value = ""
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
            text = errMsgUsername.value,
            fontSize = 14.sp,
            color = Color.Red
        )

        OutlinedTextField(
            label = { Text(stringResource(R.string.password)) },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (passwordVisibility) VisualTransformation.None
            else PasswordVisualTransformation(),
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(painter = icon, contentDescription = stringResource(R.string.visible_pass))
                }
            },
            isError = !isValidPassword.value,
            value = password,
            onValueChange = {
                password = it
                if (password.contains(" ")) {
                    isValidPassword.value = false
                    errMsgPassword.value = msgError[2]
                } else if (!isValidPassword(password)) {
                    isValidPassword.value = false
                    errMsgPassword.value = msgError[1]
                } else {
                    isValidPassword.value = true
                    errMsgPassword.value = ""
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
            text = errMsgPassword.value,
            fontSize = 14.sp,
            color = Color.Red
        )

        Spacer(modifier = modifier.height(16.dp))

        Button(
            onClick = {
                keyboardController?.hide()
                onLoginSuccess()
            },
            enabled = isValidUsername.value && isValidPassword.value,
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(stringResource(R.string.login))
        }

        Spacer(modifier = modifier.height(64.dp))

        Column(
            modifier = modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { onNavigateToCheckUsername() })
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(id = R.string.forget_pass))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    JJSembakoTheme {
        LoginScreen(onLoginSuccess = {}, onNavigateToCheckUsername = {})
    }
}