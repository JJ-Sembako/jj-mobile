package com.dr.jjsembako.feature_setting.presentation.recovery

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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.dr.jjsembako.core.data.remote.response.account.DataRecoveryQuestion
import com.dr.jjsembako.core.presentation.components.AlertErrorDialog
import com.dr.jjsembako.core.presentation.components.ErrorScreen
import com.dr.jjsembako.core.presentation.components.LoadingDialog
import com.dr.jjsembako.core.presentation.components.LoadingScreen
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.isValidAnswer
import kotlinx.coroutines.launch

@Composable
fun PemulihanAkunScreen(
    onNavigateToSetting: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pemulihanAkunViewModel: PemulihanAkunViewModel = hiltViewModel()
    val stateFirst = pemulihanAkunViewModel.stateFirst.observeAsState().value
    val stateSecond = pemulihanAkunViewModel.stateSecond.observeAsState().value
    val statusCode = pemulihanAkunViewModel.statusCode.observeAsState().value
    val message = pemulihanAkunViewModel.message.observeAsState().value
    val questionList = pemulihanAkunViewModel.questionList.observeAsState().value

    when (stateFirst) {
        StateResponse.LOADING -> {
            LoadingScreen(modifier = modifier)
        }

        StateResponse.ERROR -> {
            Log.e("PemulihanAkunScreen", "Error")
            Log.e("PemulihanAkunScreen", "state: $stateFirst")
            Log.e("PemulihanAkunScreen", "Error: $message")
            Log.e("PemulihanAkunScreen", "statusCode: $statusCode")
            ErrorScreen(
                onNavigateBack = { onNavigateToSetting() },
                onReload = { pemulihanAkunViewModel.fetchAccountRecoveryQuestions() },
                message = message ?: "Unknown error",
                modifier = modifier
            )
        }

        StateResponse.SUCCESS -> {
            if (questionList.isNullOrEmpty()) {
                ErrorScreen(
                    onNavigateBack = { onNavigateToSetting() },
                    onReload = { pemulihanAkunViewModel.fetchAccountRecoveryQuestions() },
                    message = "Server Error",
                    modifier = modifier
                )
            } else {
                when (stateSecond) {
                    StateResponse.LOADING -> {
                        LoadingScreen(modifier = modifier)
                    }

                    StateResponse.ERROR -> {
                        Log.e("PemulihanAkunScreen", "Error")
                        Log.e("PemulihanAkunScreen", "state: $stateSecond")
                        Log.e("PemulihanAkunScreen", "Error: $message")
                        Log.e("PemulihanAkunScreen", "statusCode: $statusCode")
                        ErrorScreen(
                            onNavigateBack = { onNavigateToSetting() },
                            onReload = { pemulihanAkunViewModel.fetchAccountRecovery() },
                            message = message ?: "Unknown error",
                            modifier = modifier
                        )
                    }

                    StateResponse.SUCCESS -> {
                        PemulihanAkunContent(
                            onNavigateToSetting = { onNavigateToSetting() },
                            questionList = questionList,
                            recoveryIsActive = pemulihanAkunViewModel.isActive ?: false,
                            recoveryIdQuestion = pemulihanAkunViewModel.idQuestion ?: "",
                            recoveryAnswer = pemulihanAkunViewModel.answer ?: "",
                            pemulihanAkunViewModel = pemulihanAkunViewModel,
                            modifier = modifier
                        )
                    }

                    else -> {}
                }
            }
        }

        else -> {}
    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun PemulihanAkunContent(
    onNavigateToSetting: () -> Unit,
    questionList: List<DataRecoveryQuestion?>?,
    recoveryIsActive: Boolean,
    recoveryIdQuestion: String,
    recoveryAnswer: String,
    pemulihanAkunViewModel: PemulihanAkunViewModel,
    modifier: Modifier = Modifier
) {
    val stateThird = pemulihanAkunViewModel.stateThird.observeAsState().value
    val statusCode = pemulihanAkunViewModel.statusCode.observeAsState().value
    val message = pemulihanAkunViewModel.message.observeAsState().value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    val showLoadingDialog = rememberSaveable { mutableStateOf(false) }
    val showErrorDialog = rememberSaveable { mutableStateOf(false) }

    var isExpanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<DataRecoveryQuestion?>(null) }
    var isActive by rememberSaveable { mutableStateOf(recoveryIsActive) }

    var questionId by rememberSaveable { mutableStateOf(recoveryIdQuestion) }
    var answer by rememberSaveable { mutableStateOf(recoveryAnswer) }
    var answerVisibility by remember { mutableStateOf(false) }

    val isValidAnswer = rememberSaveable { mutableStateOf(false) }
    val errMsgAnswer = rememberSaveable { mutableStateOf("") }

    val errAnswerMin3Char = stringResource(R.string.err_answer)
    val icon =
        if (answerVisibility) painterResource(id = R.drawable.ic_visibility_on) else painterResource(
            id = R.drawable.ic_visibility_off
        )

    LaunchedEffect(key1 = keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }

    if (recoveryIdQuestion.isNotEmpty()) {
        val selectedQuestion = questionList?.find { it?.id == recoveryIdQuestion }
        if (selectedQuestion != null) {
            selectedOption = selectedQuestion
        }
    }

    when (stateThird) {
        StateResponse.LOADING -> {
            showLoadingDialog.value = true
        }

        StateResponse.ERROR -> {
            Log.e("PemulihanAkunScreen", "Error")
            Log.e("PemulihanAkunScreen", "state: $stateThird")
            Log.e("PemulihanAkunScreen", "Error: $message")
            Log.e("PemulihanAkunScreen", "statusCode: $statusCode")
            showLoadingDialog.value = false
            showErrorDialog.value = true
            pemulihanAkunViewModel.setStateThird(null)
        }

        StateResponse.SUCCESS -> {
            showLoadingDialog.value = false
            showErrorDialog.value = false
            pemulihanAkunViewModel.setStateThird(null)
            onNavigateToSetting()
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
                title = { Text(stringResource(R.string.account_recovery)) },
                navigationIcon = {
                    IconButton(onClick = {
                        keyboardController?.hide()
                        onNavigateToSetting()
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
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(id = R.string.activate_account_recovery))
                Switch(checked = isActive, onCheckedChange = {
                    isActive = it
                    if (!isActive) {
                        isExpanded = false
                        selectedOption = null
                        questionId = ""
                        answer = ""
                        isValidAnswer.value = false
                        errMsgAnswer.value = ""
                    }
                })
            }
            Spacer(modifier = modifier.height(16.dp))
            Divider(
                modifier = modifier
                    .fillMaxWidth()
                    .width(1.dp), color = MaterialTheme.colorScheme.tertiary
            )

            Spacer(modifier = modifier.height(48.dp))
            if (isActive) {
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                ) {
                    TextField(
                        placeholder = { Text(text = stringResource(R.string.choose_question)) },
                        value = selectedOption?.question
                            ?: stringResource(R.string.choose_question),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        if (questionList?.isNotEmpty() == true) {
                            questionList.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(text = option?.question ?: "") },
                                    onClick = {
                                        selectedOption = option
                                        questionId = option?.id ?: ""
                                        isExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    label = { Text(stringResource(R.string.answer)) },
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if (answerVisibility) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { answerVisibility = !answerVisibility }) {
                            Icon(
                                painter = icon,
                                contentDescription = stringResource(R.string.visible_answer)
                            )
                        }
                    },
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                    text = errMsgAnswer.value,
                    fontSize = 14.sp,
                    color = Color.Red
                )
            }
            Spacer(modifier = modifier.height(48.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    if (isActive) pemulihanAkunViewModel.handleActivateAccountRecovery(
                        questionId,
                        answer
                    )
                    else pemulihanAkunViewModel.handleDeactivateAccountRecovery()
                },
                enabled = (if (isActive) isValidAnswer.value && questionId.isNotEmpty() else true) && stateThird != StateResponse.LOADING,
                modifier = modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(stringResource(R.string.save))
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
private fun PemulihanAkunScreenPreview() {
    JJSembakoTheme {
        PemulihanAkunScreen(onNavigateToSetting = {})
    }
}