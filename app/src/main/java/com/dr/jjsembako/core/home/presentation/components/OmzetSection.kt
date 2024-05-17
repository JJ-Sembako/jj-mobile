package com.dr.jjsembako.core.home.presentation.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.formatRupiah

@Composable
fun OmzetSection(omzet: Long = 0, isErrorInit: Boolean = false, modifier: Modifier) {
    val omzetVisibility = rememberSaveable { mutableStateOf(false) }
    val icon =
        if (omzetVisibility.value) painterResource(id = R.drawable.ic_visibility_on)
        else painterResource(id = R.drawable.ic_visibility_off)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp),
    ) {
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.turnover_this_month), fontWeight = FontWeight.Bold
            )
            if (isErrorInit) {
                Text(text = stringResource(R.string.error_string))
            } else {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (omzetVisibility.value) {
                        Text(text = formatRupiah(omzet))
                    } else {
                        Text(text = stringResource(R.string.hidden_value))
                    }
                    IconButton(
                        onClick = { omzetVisibility.value = !omzetVisibility.value },
                        modifier = modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            painter = icon, modifier = modifier.size(24.dp),
                            tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
                            else Color.Black,
                            contentDescription = stringResource(R.string.change_visibility_omzet)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun OmzetSectionPreview() {
    JJSembakoTheme {
        OmzetSection(omzet = 123456789, modifier = Modifier)
    }
}