package com.dr.jjsembako.core.utils.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.dr.jjsembako.ProductOrderList
import java.io.InputStream
import java.io.OutputStream

class ProductOrderStoreSerializer : Serializer<ProductOrderList> {
    override val defaultValue: ProductOrderList = ProductOrderList.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ProductOrderList {
        try {
            return ProductOrderList.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: ProductOrderList, output: OutputStream) {
        t.writeTo(output)
    }
}