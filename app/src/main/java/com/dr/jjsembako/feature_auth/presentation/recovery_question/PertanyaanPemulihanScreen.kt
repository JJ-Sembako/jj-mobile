package com.dr.jjsembako.feature_auth.presentation.recovery_question

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dr.jjsembako.R
import com.dr.jjsembako.core.common.StateResponse
import com.dr.jjsembako.core.presentation.components.AlertErrorDialog
import com.dr.jjsembako.core.presentation.components.ErrorScreen
import com.dr.jjsembako.core.presentation.components.LoadingDialog
import com.dr.jjsembako.core.presentation.components.LoadingScreen
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.isValidAnswer
import kotlinx.coroutines.launch

@Composable
fun PertanyaanPemulihanScreen(
    username: String,
    onNavigateBack: () -> Unit,
    onNavigateToChangePassword: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val pertanyaanPemulihanViewModel: PertanyaanPemulihanViewModel = hiltViewModel()
    val stateFirst = pertanyaanPemulihanViewModel.stateFirst.observeAsState().value
    val statusCode = pertanyaanPemulihanViewModel.statusCode.observeAsState().value
    val message = pertanyaanPemulihanViewModel.message.observeAsState().value
    val question = pertanyaanPemulihanViewModel.question

    // Set username for the first time Composable is rendered
    LaunchedEffect(username) {
        pertanyaanPemulihanViewModel.setUsername(username)
    }

    when (stateFirst) {
        StateResponse.LOADING -> {
            LoadingScreen(modifier = modifier)
        }

        StateResponse.ERROR -> {
            Log.e("pertanyaanPemulihan", "Error")
            Log.e("pertanyaanPemulihan", "state: $stateFirst")
            Log.e("pertanyaanPemulihan", "Error: $message")
            Log.e("pertanyaanPemulihan", "statusCode: $statusCode")
            ErrorScreen(
                onNavigateBack = { onNavigateBack() },
                onReload = {
                    pertanyaanPemulihanViewModel.fetchAccountRecoveryQuestionByUsername(
                        username
                    )
                },
                message = message ?: "Unknown error",
                modifier = modifier
            )
        }

        StateResponse.SUCCESS -> {
            if (question?.isEmpty() == true) {
                ErrorScreen(
                    onNavigateBack = { onNavigateBack() },
                    onReload = {
                        pertanyaanPemulihanViewModel.fetchAccountRecoveryQuestionByUsername(
                            username
                        )
                    },
                    message = "Server Error",
                    modifier = modifier
                )
            } else {
                PertanyaanPemulihanContent(
                    username = username,
                    recoveryQuestion = question ?: "",
                    pertanyaanPemulihanViewModel = pertanyaanPemulihanViewModel,
                    onNavigateBack = onNavigateBack,
                    onNavigateToChangePassword = onNavigateToChangePassword,
                    modifier = modifier
                )
            }
        }

        else -> {}
    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PertanyaanPemulihanContent(
    username: String,
    recoveryQuestion: String,
    pertanyaanPemulihanViewModel: PertanyaanPemulihanViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToChangePassword: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val stateSecond = pertanyaanPemulihanViewModel.stateSecond.observeAsState().value
    val statusCode = pertanyaanPemulihanViewModel.statusCode.observeAsState().value
    val message = pertanyaanPemulihanViewModel.message.observeAsState().value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    var showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    var showErrorDialog = rememberSaveable { mutableStateOf(false) }

    var question by rememberSaveable { mutableStateOf(recoveryQuestion) }
    var answer by rememberSaveable { mutableStateOf("") }
    var isValidAnswer = rememberSaveable { mutableStateOf(false) }
    var errMsgAnswer = rememberSaveable { mutableStateOf("") }

    val errQuestionNotFound = stringResource(R.string.err_question_not_found)
    val errAnswerMin3Char = stringResource(R.string.err_answer)

    LaunchedEffect(key1 = keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }

    when (stateSecond) {
        StateResponse.LOADING -> {
            showLoadingDialog.value = true
        }

        StateResponse.ERROR -> {
            Log.e("PemulihanAkunScreen", "Error")
            Log.e("PemulihanAkunScreen", "state: $stateSecond")
            Log.e("PemulihanAkunScreen", "Error: $message")
            Log.e("PemulihanAkunScreen", "statusCode: $statusCode")
            showLoadingDialog.value = false
            showErrorDialog.value = true
            pertanyaanPemulihanViewModel.setStateSecond(null)
        }

        StateResponse.SUCCESS -> {
            showLoadingDialog.value = false
            showErrorDialog.value = false
            pertanyaanPemulihanViewModel.setStateSecond(null)
            onNavigateToChangePassword(username)
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
                title = { Text(stringResource(R.string.question_recovery)) },
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
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                text = stringResource(R.string.question),
                fontSize = 14.sp,
            )
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 24.dp),
                text = question.ifEmpty { errQuestionNotFound },
            )
            OutlinedTextField(
                label = { Text(stringResource(R.string.answer)) },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                isError = !isValidAnswer.value,
                value = answer,
                onValueChange = {
                    answer = it
                    if (!isValidAnswer(answer)) {
                        isValidAnswer.value = false
                        errMsgAnswer.value = errAnswerMin3Char
                    } else {
                        isValidAnswer.value = true
                        errMsgAnswer.value = ""
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
                text = errMsgAnswer.value,
                fontSize = 14.sp,
                color = Color.Red
            )

            Spacer(modifier = modifier.height(16.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    pertanyaanPemulihanViewModel.checkAccountRecoveryAnswer(username, answer)
                },
                enabled = isValidAnswer.value,
                modifier = modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(stringResource(R.string.verification))
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

@Preview(showBackground = true)
@Composable
fun PertanyaanPemulihanScreenPreview() {
    JJSembakoTheme {
        PertanyaanPemulihanScreen(
            username = "",
            onNavigateBack = {},
            onNavigateToChangePassword = {}
        )
    }
}