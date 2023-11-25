package com.dr.jjsembako.ui.feature_auth.check_username

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dr.jjsembako.R
import com.dr.jjsembako.ui.theme.JJSembakoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PengecekanUsernameScreen(
    onNavigateToLogin: () -> Unit, onNavigateToCheckAnswer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = { Text(stringResource(R.string.check_username)) },
                navigationIcon = {
                    IconButton(onClick = { onNavigateToLogin() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(modifier = modifier.padding(contentPadding)) {
            Text(text = "Cek Username")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PengecekanUsernameScreenScreenPreview() {
    JJSembakoTheme {
        PengecekanUsernameScreen(onNavigateToLogin = {}, onNavigateToCheckAnswer = {})
    }
}