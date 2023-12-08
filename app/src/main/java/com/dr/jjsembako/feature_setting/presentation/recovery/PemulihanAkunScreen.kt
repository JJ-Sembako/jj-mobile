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
import com.dr.jjsembako.core.data.model.DropDownOption
import com.dr.jjsembako.core.utils.isValidAnswer
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
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

    when (stateFirst) {
        StateResponse.LOADING -> {

        }

        StateResponse.ERROR -> {
            Log.e("LoginScreen", "Error")
            Log.e("LoginScreen", "state: $stateFirst")
            Log.e("LoginScreen", "Error: $message")
            Log.e("LoginScreen", "statusCode: $statusCode")
        }

        StateResponse.SUCCESS -> {

        }

        else -> {}
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PemulihanAkunContent(
    onNavigateToSetting: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    var isExpanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<DropDownOption?>(null) }
    var isActive by rememberSaveable { mutableStateOf(false) }

    var questionId by rememberSaveable { mutableStateOf("") }
    var answer by rememberSaveable { mutableStateOf("") }
    var answerVisibility by remember { mutableStateOf(false) }

    var isValidAnswer = rememberSaveable { mutableStateOf(false) }
    var errMsgAnswer = rememberSaveable { mutableStateOf("") }

    val errAnswerMin3Char = stringResource(R.string.err_answer)
    var icon =
        if (answerVisibility) painterResource(id = R.drawable.ic_visibility_on) else painterResource(
            id = R.drawable.ic_visibility_off
        )

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
                        value = selectedOption?.option ?: stringResource(R.string.choose_question),
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
                        dropDownOption.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option.option) },
                                onClick = {
                                    selectedOption = option
                                    questionId = option.value
                                    isExpanded = false
                                }
                            )
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
                    onNavigateToSetting()
                },
                enabled = if (isActive) isValidAnswer.value && questionId.isNotEmpty() else true,
                modifier = modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(stringResource(R.string.save))
            }
        }

    }
}

private val dropDownOption = listOf(
    DropDownOption("Siapa nama kamu?", "abcd-1234-1"),
    DropDownOption("Siapa nama peliharaanmu?", "wxyz-4567-1")
)

@Preview(showBackground = true)
@Composable
fun PemulihanAkunScreenPreview() {
    JJSembakoTheme {
        PemulihanAkunScreen(onNavigateToSetting = {})
    }
}