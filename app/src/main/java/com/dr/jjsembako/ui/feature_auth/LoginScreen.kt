package com.dr.jjsembako.ui.feature_auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dr.jjsembako.R
import com.dr.jjsembako.ui.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.isValidUsername
import com.dr.jjsembako.core.utils.isValidPassword

@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    var icon =
        if (passwordVisibility) painterResource(id = R.drawable.ic_visibility_on) else painterResource(
            id = R.drawable.ic_visibility_off
        )
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_hi))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )
    var isValidUsername = rememberSaveable { mutableStateOf(false) }
    var errMsgUsername = rememberSaveable { mutableStateOf("") }
    var isValidPassword = rememberSaveable { mutableStateOf(false) }
    var errMsgPassword = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            enableMergePaths = true,
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.selamat_datang),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            label = { Text(stringResource(R.string.username)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            isError = !isValidUsername.value,
            value = username,
            onValueChange = {
                username = it
                if (!isValidUsername(username)) {
                    isValidUsername.value = false
                    errMsgUsername.value =
                        "Username minimal 5 karakter diawali huruf diikuti karakter huruf, angka, underscore, atau strip!"
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            text = errMsgUsername.value,
            fontSize = 14.sp,
            color = Color.Red
        )

        OutlinedTextField(
            label = { Text(stringResource(R.string.password)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                if (!isValidPassword(password)) {
                    isValidPassword.value = false
                    errMsgPassword.value = "Password minimal 8 karakter!"
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            text = errMsgPassword.value,
            fontSize = 14.sp,
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onLoginSuccess()
            },
            enabled = isValidUsername.value && isValidPassword.value,
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(stringResource(R.string.masuk))
        }

        Spacer(modifier = modifier.height(64.dp))

        Text(
            text = stringResource(id = R.string.lupa_pass),
            modifier = modifier
                .padding(bottom = 24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    JJSembakoTheme {
        LoginScreen(navController = rememberNavController(), onLoginSuccess = {})
    }
}