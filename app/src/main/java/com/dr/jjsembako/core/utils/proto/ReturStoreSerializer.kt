package com.dr.jjsembako.core.utils.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.dr.jjsembako.ReturStore
import java.io.InputStream
import java.io.OutputStream

class ReturStoreSerializer : Serializer<ReturStore> {
    override val defaultValue: ReturStore = ReturStore.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ReturStore {
        try {
            return ReturStore.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: ReturStore, output: OutputStream) {
        t.writeTo(output)
    }
}