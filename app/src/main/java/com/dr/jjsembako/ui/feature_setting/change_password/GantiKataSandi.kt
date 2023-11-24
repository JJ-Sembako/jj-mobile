package com.dr.jjsembako.ui.feature_setting.change_password

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GantiKataSandiScreen(navController: NavController){
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var oldPassword by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confNewPassword by rememberSaveable { mutableStateOf("") }

    var isValidOldPassword = rememberSaveable { mutableStateOf(false) }
    var errMsgOldPassword = rememberSaveable { mutableStateOf("") }
    var isValidNewPassword = rememberSaveable { mutableStateOf(false) }
    var errMsgNewPassword = rememberSaveable { mutableStateOf("") }
    var isValidConfNewPassword = rememberSaveable { mutableStateOf(false) }
    var errMsgConfNewPassword = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { keyboardController?.hide() })
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){}
}