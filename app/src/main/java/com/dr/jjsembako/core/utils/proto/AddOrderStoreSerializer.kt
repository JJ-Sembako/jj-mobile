package com.dr.jjsembako.core.utils.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.dr.jjsembako.AddOrderStore
import java.io.InputStream
import java.io.OutputStream

class AddOrderStoreSerializer : Serializer<AddOrderStore> {
    override val defaultValue: AddOrderStore = AddOrderStore.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AddOrderStore {
        try {
            return AddOrderStore.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: AddOrderStore, output: OutputStream) {
        t.writeTo(output)
    }
}