package com.dr.jjsembako.core.utils.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.dr.jjsembako.CanceledStore
import java.io.InputStream
import java.io.OutputStream

class CanceledStoreSerializer : Serializer<CanceledStore> {
    override val defaultValue: CanceledStore = CanceledStore.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): CanceledStore {
        try {
            return CanceledStore.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: CanceledStore, output: OutputStream) {
        t.writeTo(output)
    }
}