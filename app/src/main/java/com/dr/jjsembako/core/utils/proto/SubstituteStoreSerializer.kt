package com.dr.jjsembako.core.utils.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.dr.jjsembako.SubstituteStore
import java.io.InputStream
import java.io.OutputStream

class SubstituteStoreSerializer : Serializer<SubstituteStore> {
    override val defaultValue: SubstituteStore = SubstituteStore.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SubstituteStore {
        try {
            return SubstituteStore.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: SubstituteStore, output: OutputStream) {
        t.writeTo(output)
    }
}