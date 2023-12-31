package com.dr.jjsembako.feature_home.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.utils.formatRupiah
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun OmzetSection(omzet: Long = 0, modifier: Modifier) {
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
            Text(text = formatRupiah(omzet))
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