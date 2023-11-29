package com.dr.jjsembako.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dr.jjsembako.R
import com.dr.jjsembako.ui.theme.JJSembakoTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchFilter(
    activeSearch: MutableState<Boolean>,
    searchQuery: MutableState<String>,
    openFilter: () -> Unit,
    modifier: Modifier
) {
    val focusManager = LocalFocusManager.current
    val placeholder = stringResource(R.string.search_cust)
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DockedSearchBar(
            modifier = modifier
                .weight(1f)
                .heightIn(min = 48.dp),
            query = searchQuery.value,
            onQueryChange = {
                searchQuery.value = it
                activeSearch.value = searchQuery.value != ""
            },
            onSearch = {
                keyboardController?.hide()
                focusManager.clearFocus()
                activeSearch.value = false
            },
            active = false,
            onActiveChange = {
                activeSearch.value = it
            },
            placeholder = {
                Text(text = placeholder)
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search))
            },
            trailingIcon = {
                if (activeSearch.value || searchQuery.value.isNotEmpty()) {
                    Icon(
                        modifier = modifier.clickable {
                            if (searchQuery.value.isNotEmpty()) {
                                searchQuery.value = ""
                            } else {
                                activeSearch.value = false
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close_search)
                    )
                }
            },
        ) {
            Column(modifier = modifier.height(0.dp)) {

            }
        }

        // Filter icon
        if (!activeSearch.value) {
            Spacer(modifier = modifier.width(12.dp))
            Column(modifier = modifier.padding(top = 8.dp)) {
                IconButton(
                    onClick = { openFilter() }, modifier = modifier
                        .clip(RoundedCornerShape(16.dp))
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        Icons.Default.FilterAlt,
                        contentDescription = stringResource(R.string.filter),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

    }
}

@Composable
@Preview(showBackground = true)
fun SearchFilterPreview() {
    JJSembakoTheme {
        SearchFilter(
            activeSearch = remember { mutableStateOf(false) },
            searchQuery = remember { mutableStateOf("") },
            openFilter = {},
            modifier = Modifier
        )
    }
}