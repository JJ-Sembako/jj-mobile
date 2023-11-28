package com.dr.jjsembako.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dr.jjsembako.R
import com.dr.jjsembako.ui.theme.JJSembakoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilter(openFilter: ()->Unit, modifier: Modifier) {
    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val placeholder = stringResource(R.string.search_cust)

    Row(
        modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Adjusted horizontal arrangement
    ) {
        // SearchBar
        SearchBar(
            modifier = modifier.weight(1f),
            query = searchQuery,
            onQueryChange = {
                searchQuery = it
            },
            onSearch = {
                active = false
            },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = {
                Text(text = placeholder)
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search))
            },
            trailingIcon = {
                if (active) {
                    Icon(
                        modifier = modifier.clickable {
                            if (searchQuery.isNotEmpty()) {
                                searchQuery = ""
                            } else {
                                active = false
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close_search)
                    )
                }
            }
        ) {}

        Spacer(modifier = modifier.width(12.dp))

        // Filter icon
        Column(modifier = modifier.padding(top = 8.dp)){
            IconButton(
                onClick = {openFilter()}, modifier = modifier
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

@Composable
@Preview(showBackground = true)
fun SearchFilterPreview() {
    JJSembakoTheme {
        SearchFilter(openFilter = {}, modifier = Modifier)
    }
}