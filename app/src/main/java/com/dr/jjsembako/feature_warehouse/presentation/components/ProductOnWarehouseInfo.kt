package com.dr.jjsembako.feature_warehouse.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dr.jjsembako.R
import com.dr.jjsembako.core.data.model.Product
import com.dr.jjsembako.core.presentation.theme.JJSembakoTheme
import com.dr.jjsembako.core.utils.formatRupiah

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
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
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
        if(product.image.isEmpty()){
            AsyncImage(
                model = painterResource(id = R.drawable.ic_default),
                contentDescription = stringResource(R.string.product_description, product.name),
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .padding(8.dp)
                    .width(60.dp)
                    .height(80.dp)
                    .clip(CircleShape)
            )
        }else {
            AsyncImage(
                model = product.image,
                contentDescription = stringResource(R.string.product_description, product.name),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ic_error),
                modifier = modifier
                    .padding(8.dp)
                    .width(60.dp)
                    .height(80.dp)
                    .clip(CircleShape)
            )
        }
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
            text = stringResource(R.string.standard_price, formatRupiah(product.standardPrice)),
            fontWeight = FontWeight.Medium, fontSize = 12.sp
        )
        Text(
            text = stringResource(R.string.stock, product.stock),
            fontSize = 12.sp
        )
        Text(
            text = stringResource(R.string.unit, product.unit),
            fontSize = 12.sp
        )
        Text(
            text = stringResource(R.string.amount_per_unit, product.amountPerUnit),
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