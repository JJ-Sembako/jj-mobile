package com.dr.jjsembako.feature_warehouse.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.model.Product
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme

@Composable
fun ProductOnWarehouseInfo(
    product: Product,
    modifier: Modifier
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
        ) {
            ProductImage(product = product, modifier = modifier)
            Spacer(modifier = modifier.width(16.dp))
            ProductInfo(product = product)
        }
    }
}

@Composable
private fun ProductImage(
    product: Product,
    modifier: Modifier
) {
    Column {

    }
}

@Composable
private fun ProductInfo(
    product: Product
) {
    Column {
        Text(
            text = product.name, fontWeight = FontWeight.Bold, fontSize = 14.sp,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )
        Text(
            text = stringResource(R.string.stock, product.stock),
            fontSize = 12.sp
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ProductOnWarehouseInfoPreview() {
    JJSembakoTheme {
        ProductOnWarehouseInfo(
            product = Product(
                id = "bc3bbd9e",
                name = "Air Cahaya",
                stock = 256,
                standardPrice = 55000,
                amountPerUnit = 16,
                image = "",
                unit = "karton",
                category = "Air Mineral"
            ),
            modifier = Modifier
        )
    }
}