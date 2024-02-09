package com.dr.jjsembako.core.utils.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.dr.jjsembako.UpdateOrderStore
import java.io.InputStream
import java.io.OutputStream

class UpdateOrderStoreSerializer : Serializer<UpdateOrderStore> {
    override val defaultValue: UpdateOrderStore = UpdateOrderStore.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UpdateOrderStore {
        try {
            return UpdateOrderStore.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UpdateOrderStore, output: OutputStream) {
        t.writeTo(output)
    }
}